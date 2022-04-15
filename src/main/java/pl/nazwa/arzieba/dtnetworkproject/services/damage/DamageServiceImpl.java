package pl.nazwa.arzieba.dtnetworkproject.services.damage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.*;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.model.Status;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.generator.GeneratorService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DamageNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.utils.mail.EmailConfiguration;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DamageServiceImpl implements DamageService {

    //---------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    private final DeviceDAO deviceDAO;
    private final DamageDAO damageDAO;
    private final IssueDocumentDAO issueDocumentDAO;
    private final EmailConfiguration emailConfiguration;
    private final ApplicationArguments applicationArguments;
    private final ShortPostService postService;
    private final DeviceService deviceService;
    private GeneratorService generatorService;

    //-----------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------------

    @Autowired
    public DamageServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO,
                             EmailConfiguration emailConfiguration,
                             ApplicationArguments applicationArguments, ShortPostService postService,
                             DeviceService deviceService, GeneratorService generatorService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.emailConfiguration = emailConfiguration;
        this.applicationArguments = applicationArguments;
        this.postService = postService;
        this.deviceService = deviceService;
        this.generatorService = generatorService;
    }

    //-----------------------------------------------------------------DAO METHODS---------------------------------------------------------------------------------------------

    @Override
    public List<DamageDTO> findAll() {
        return damageDAO.findAll()
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public DamageDTO findById(Integer id) {
        return damageDAO.findById(id)
                .map(DamageMapper::map).orElse(null);
    }

    @Override
    public int numberOfDamagesByDevice(String inventNumber){
        return damageDAO.findByDevice_InventNumber(inventNumber).size();
    }

    @Override
    public List<Damage> findByDeviceInventNumber(int page, int size,String inventNumber) {
        return damageDAO.findByDevice_InventNumber(PageRequest.of(page, size, Sort.Direction.DESC,
                        "damageDate"), inventNumber)
                                    .get()
                                    .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByDateBefore(String date) {
        return damageDAO.findByDamageDateBefore(CalendarUtil.string2cal(date))
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByDateAfter(String date) {
        return damageDAO.findByDamageDateAfter(CalendarUtil.string2cal(date))
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByAuthor(String author) {
        return damageDAO.findByAuthor(Author.valueOf(author)).stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public DamageDTO create(DamageDTO damageDTO) {
       Damage saved = damageDAO.save(DamageMapper.map(damageDTO, deviceDAO));
       damageDTO.setDamageId(saved.getDamageId());

       if (!damageDTO.getDescription().contains("[SYSTEM]")){
           emailConfiguration.sendMail(
           "AWARIA dla: "+deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber())
           .getDeviceDescription()+ " w: "+deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()).getRoom().name(),damageDTO.getDescription()
           ,damageDTO.getAuthor().name());
       }
        return damageDTO;
    }

    @Override
    public DamageDTO update(DamageDTO damageDTO) throws NullPointerException {

        if(!findAll().stream()
                .map(DamageDTO::getDamageId)
                .collect(Collectors.toList())
                .contains(damageDTO.getDamageId())) return  create(damageDTO);
         else {
            Damage toBeUpdated = damageDAO.findById(damageDTO.getDamageId()).orElse(null);
            String lastDescription = toBeUpdated.getDescription();
            String dateOfUpdating = damageDTO.getDamageDate();
            toBeUpdated.setDescription(lastDescription
                    +"\n\n"+"[UPDATE] "+dateOfUpdating+"\n"+ damageDTO.getDescription());
            toBeUpdated.setAuthor(damageDTO.getAuthor());
            damageDAO.save(toBeUpdated);

                if (!damageDTO.getDescription().contains("[SYSTEM]")) {
                    emailConfiguration.sendMail("AWARIA dla: " + deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()).getDeviceDescription() + " w: " + deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()).getRoom().name() + " [UPDATE]", damageDTO.getDescription()
                            , damageDTO.getAuthor().name());
                }
            return DamageMapper.map(toBeUpdated);
        }
    }

    @Override
    public DamageDTO remove(Integer id) {

        if(!findAll().stream()
        .map(DamageDTO::getDamageId)
            .collect(Collectors.toList())
            .contains(id)) throw new DamageNotFoundException("Damage not found");
        else{
            DamageDTO removed = findById(id);
            damageDAO.deleteById(id);
            removed.setDescription("Removed "+removed.getDamageId());
            return removed;
        }
    }

    @Override
    public List<IssueDocumentDTO> getIssueDocuments(Integer id) {
        return issueDocumentDAO.findAll().stream()
                .filter(d->d.getDamage().getDamageId().equals(id))
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
}

//--------------------------------------------------------------------CONTROLLER METHODS----------------------------------------------------------------------------------------
@Override
@Transactional
public String addGeneratorActivity(GeneratorTestDTO testDTO, BindingResult bindingResult, Model model) {

    if(bindingResult.hasFieldErrors()){
        List<FieldError> allErrors;
        allErrors = bindingResult.getFieldErrors();
        model.addAttribute("bindingResult", bindingResult);
        model.addAttribute("errors",allErrors);
        model.addAttribute("errorsAmount",allErrors.size());
        return addActFormErr(model, testDTO.getInventNumber(), testDTO );
    }

    if( testDTO.isLossPowerFlag()==false && testDTO.getStatus()!= Status.DAMAGE ) {
        log.info("");
        generatorService.create(testDTO);
        return "redirect:/generators/" + testDTO.getInventNumber()+"/1";
    }

    if(testDTO.isLossPowerFlag()==true && testDTO.getStatus()!= Status.DAMAGE){
        ShortPostDTO postDTO = new ShortPostDTO(Author.DTN,"Generator podał napięcie podczas zaniku! [SYSTEM]",testDTO.getDate(),testDTO.getInventNumber(),false,PostLevel.POWER);
        generatorService.create(testDTO);
        postService.create(postDTO);
        return "redirect:/generators/" + testDTO.getInventNumber()+"/1";
    }

    if(testDTO.isLossPowerFlag()==false && testDTO.getStatus()== Status.DAMAGE){
        DamageDTO damageDTO = new DamageDTO(testDTO.getContent(),testDTO.getDate(),Author.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()),testDTO.getInventNumber(),true);
        ShortPostDTO postDTO = new ShortPostDTO(Author.DTN,"Nowa usterka! Szczegóły po kliknięciu w Urządzenie... [SYSTEM]",damageDTO.getDamageDate(),damageDTO.getDeviceInventNumber(),true,PostLevel.DAMAGE);
        generatorService.create(testDTO);
        create(damageDTO);
        postService.create(postDTO);
        return "redirect:/dtnetwork";
    }

    if(testDTO.isLossPowerFlag()==true && testDTO.getStatus()== Status.DAMAGE){
        ShortPostDTO postDTOForPower = new ShortPostDTO(Author.DTN,"Generator podał napięcie podczas zaniku! [SYSTEM]",testDTO.getDate(),testDTO.getInventNumber(),false,PostLevel.POWER);
        DamageDTO damageDTO = new DamageDTO(testDTO.getContent(),testDTO.getDate(),Author.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()),testDTO.getInventNumber(),true);
        ShortPostDTO postDTOForDamage = new ShortPostDTO(Author.DTN,"Nowa usterka! Szczegóły po kliknięciu w Urządzenie... [SYSTEM]",damageDTO.getDamageDate(),damageDTO.getDeviceInventNumber(),true,PostLevel.DAMAGE);
        generatorService.create(testDTO);
        postService.create(postDTOForPower);
        create(damageDTO);
        postService.create(postDTOForDamage);
        return "redirect:/dtnetwork";
    }
    return "redirect:/error";
}

    @Override
    @Transactional
    public String addAsModel(DamageDTO damageDTO, BindingResult bindingResult, Model model) {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return createDamageErr(model,damageDTO.getDeviceInventNumber(),damageDTO);
        }

        if(!damageDTO.isNewPostFlag()) {
            create(damageDTO);
        } else{
            ShortPostDTO shortPostDTO = new ShortPostDTO(Author.DTN,"Nowa usterka! Szczegóły po kliknięciu w Urządzenie... [SYSTEM]",
                    damageDTO.getDamageDate(),damageDTO.getDeviceInventNumber(),true,PostLevel.DAMAGE);
            create(damageDTO);
            postService.create(shortPostDTO);
        }
        return "redirect:/damages/devices/"+damageDTO.getDeviceInventNumber()+"/1";
    }

    @Override
    public String createDamageErr(Model model, String inventNumber, DamageDTO damageDTO) {
        model.addAttribute("newDamage", damageDTO);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);
        return "damages/addForm";
    }

    @Override
    public String getAllForDevice(String inventNumber, Model model, int page) {
        List<Damage> damagesForDevice = findByDeviceInventNumber(page-1,10,inventNumber);
        DeviceDTO deviceDTO = deviceService.generateMainViewForDevice(inventNumber);
        Map<Integer,DamageDTO> mapOfDamageDTO = new LinkedHashMap<>();
        int numberOfPages = (numberOfDamagesByDevice(inventNumber))/10 + 1;
        List<Integer> morePages = new LinkedList<>();

        for (Damage damage: damagesForDevice) {
            mapOfDamageDTO.put(damage.getDamageId(), DamageMapper.map(damage));
        }

        if(numberOfDamagesByDevice(inventNumber)%10==0){
            numberOfPages--;
        }

        int i = 2;
        int lastPage = 1;

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("amount",numberOfDamagesByDevice(inventNumber));
        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("inventNumber",inventNumber);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("damages", mapOfDamageDTO);
        model.addAttribute("dto", deviceDTO);
        return "damages/allDamagesForDevice";
    }

    @Override
    public String createDamage(Model model, String inventNumber) {
        DamageDTO damageDTO = new DamageDTO();
        damageDTO.setDamageDate(CalendarUtil.cal2string(Calendar.getInstance()));
        model.addAttribute("newDamage", damageDTO);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String content = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text", content);
        return "damages/addForm";
    }

    @Override
    public String editDamage(Model model, Integer id) {
        DamageDTO damageDTO = findById(id);
        String inventNumber = damageDTO.getDeviceInventNumber();
        model.addAttribute("newDamage", damageDTO);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);
        model.addAttribute("damageId", id);
        return "damages/editForm";
    }

    @Override
    public String saveEditedDamage(DamageDTO damageDTO, BindingResult bindingResult, Integer id, Model model) {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return editDamageErr(model,damageDTO.getDeviceInventNumber(),damageDTO, id);
        }

        Damage damage = damageDAO.findByDamageId(id);
        damage.setAuthor(damageDTO.getAuthor());
        damage.setDescription(damageDTO.getDescription());
        damage.setDamageDate(CalendarUtil.string2cal(damageDTO.getDamageDate()));
        damage.setDevice(deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()));
        damageDAO.save(damage);

        if (!damageDTO.getDescription().contains("[SYSTEM]")){
                emailConfiguration.sendMail("AWARIA dla: "+deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()).getDeviceDescription()+ " w: "+deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()).getRoom().name()+" [UPDATE]",damageDTO.getDescription()
                        ,damageDTO.getAuthor().name());
        }
        /*damageService.update(damageDTO);*/
        return "redirect:/damages/devices/"+damageDTO.getDeviceInventNumber()+"/1";
    }

    public String editDamageErr(Model model, String inventNumber, DamageDTO damageDTO, Integer id ){
        model.addAttribute("newDamage", damageDTO);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);
        model.addAttribute("damageId", id);
        return "damages/editForm";
    }

    public String addActFormErr(Model model, String inv, GeneratorTestDTO testDTO ){
        model.addAttribute("newTest", testDTO);
        model.addAttribute("device", inv);
        model.addAttribute("text",deviceDAO.findByInventNumber(inv).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inv).getRoom() );
        return "devices/addActForm";
    }
}
