package pl.nazwa.arzieba.dtnetworkproject.services.device;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.*;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.generator.GeneratorService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.chillerSet.ChillerSetMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.deviceCard.DeviceCardMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.drycoolerSet.DrycoolerSetMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DeviceNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest.GeneratorTestMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private GeneratorTestDAO generatorTestDAO;
    private ShortPostDAO shortPostDAO;
    private GeneratorService generatorService;
    private ShortPostService postService;
    private ChillerSetDAO chillerSetDAO;
    private DrycoolerSetDAO drycoolerSetDAO;
    @Value("${my.pagesize}")
    private int pagesize;

    @Autowired
    public DeviceServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, GeneratorTestDAO generatorTestDAO, ShortPostDAO shortPostDAO, GeneratorService generatorService, ShortPostService postService,  ChillerSetDAO chillerSetDAO, DrycoolerSetDAO drycoolerSetDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.generatorTestDAO = generatorTestDAO;
        this.shortPostDAO = shortPostDAO;
        this.generatorService = generatorService;
        this.postService = postService;


        this.chillerSetDAO = chillerSetDAO;
        this.drycoolerSetDAO = drycoolerSetDAO;
    }

    @Override
    public List<DeviceDTO> findAll() {

       return deviceDAO.findAll()
               .stream()
               .map(DeviceMapper::map)
               .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO generateMainViewForDevice(String inventNumber) {

        if(!this.findAll().stream()
                .map(DeviceDTO::getInventNumber)
                .collect(Collectors.toList())
                .contains(inventNumber)) throw new DeviceNotFoundException();

        return DeviceMapper.map( deviceDAO.findByInventNumber(inventNumber));
    }

    @Override
    public List<DeviceDTO> findByType(String name) {

      return deviceDAO.findAll().stream()
              .filter(d->d.getDeviceType()
              .name().equals(name))
              .map(DeviceMapper::map)
              .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO create(DeviceDTO toAdd) {
        deviceDAO.save( DeviceMapper.map(toAdd));

        if (toAdd.getDeviceType().name()=="GENERATOR"){
            GeneratorTestDTO generatorTestDTO = new GeneratorTestDTO();
            generatorTestDTO.setAlerted(true);
            generatorTestDTO.setContent("[SYSTEM ENTRY - DO NOT REMOVE!]");
            generatorTestDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
            generatorTestDTO.setInventNumber(toAdd.getInventNumber());
            generatorTestDTO.setStatus(Status.OK);
            generatorTestDTO.setLossPowerFlag(false);
            generatorTestDAO.save(GeneratorTestMapper.map(generatorTestDTO,deviceDAO));
        }

        return toAdd;
    }

    @Override
    public DeviceDTO update(DeviceDTO deviceDTO) {

        if(!this.findAll().stream()
                .map(DeviceDTO::getInventNumber)
                .collect(Collectors.toList())
                .contains(deviceDTO.getInventNumber())){

            return  create(deviceDTO);

        } else{
           Device toBeUpdated = deviceDAO.findByInventNumber(deviceDTO.getInventNumber());

           toBeUpdated.setDeviceType(deviceDTO.getDeviceType());
           toBeUpdated.setDeviceDescription(deviceDTO.getDeviceDescription());
           toBeUpdated.setRoom(deviceDTO.getRoom());
           deviceDAO.save(toBeUpdated);

           return DeviceMapper.map(toBeUpdated);
        }
    }

    @Override
    public DeviceDTO remove(String inventNumber) {

        if(!this.findAll().stream()
                .map(DeviceDTO::getInventNumber)
                .collect(Collectors.toList())
                .contains(inventNumber)) throw new DeviceNotFoundException("Device not found");
        else {
            DeviceDTO removed = generateMainViewForDevice(inventNumber);

            deviceDAO.deleteDeviceByInventNumber(inventNumber);
            removed.setDeviceDescription("Removed " + removed.getDeviceDescription());

            return removed;
        }
    }

    @Override
    public List<DamageDTO> getDamages(String inventNumber) {

        return deviceDAO.findByInventNumber(inventNumber)
                .getDamageList()
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDocumentDTO> getIssueDocuments(String inventNumber) {

        return issueDocumentDAO.findByInventNumber(inventNumber).stream()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceCardDTO getDeviceCard(String inventNumber) {

        return DeviceCardMapper.map(deviceCardDAO.findByDevice_InventNumber(inventNumber));
    }

    @Override
    public List<DeviceDTO> findByRoom(String room) {

        return deviceDAO.findAll().stream().filter(d->d.getRoom().equals(Room.valueOf(room)))
                .map(DeviceMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO changeRoom(String inventNumber) {

       Device device = deviceDAO.findByInventNumber(inventNumber);


       if (!device.getDeviceType().equals(DeviceType.GENERATOR))  device.setDeviceDescription(device.getDeviceDescription()+ " ("+device.getRoom()+")");
       device.setRoom(Room.Nieaktywne);

        ShortPost shortPost = new ShortPost();
        shortPost.setAuthor(Author.DTN);
        shortPost.setDevice(device);
        shortPost.setContent("Urządzenie przeniesione do sekcji NIEAKTYWNE [SYSTEM]");
        shortPost.setPostLevel(PostLevel.INFO);
        shortPost.setPostDate(Calendar.getInstance());
        shortPost.setDate(Calendar.getInstance().getTime());
        shortPostDAO.save(shortPost);

       return DeviceMapper.map(deviceDAO.save(device));
    }
    //----------------------------------------------HTTP methods--------------------------------------------------------
    @Override
    public String generateMainViewForDevice(String inventNumber, Model model) {
        DeviceDTO deviceDTO = generateMainViewForDevice(inventNumber);
        Device device= deviceDAO.findByInventNumber(inventNumber);
        String lastTest = "Nieznana";
        String activityType = "(nieznane)";
        ChillerSetDTO chillerSetDTO = new ChillerSetDTO();
        DrycoolerSetDTO drycoolerSetDTO = new DrycoolerSetDTO();
        int posts = device.getShortPosts().size();

        if(device.getTests().size()!=0){
            lastTest = CalendarUtil.cal2string(generatorTestDAO.findTopByDevice_InventNumberOrderByDateDesc(inventNumber).getDate()) ;

            if(generatorTestDAO.findTopByDevice_InventNumberOrderByDateDesc(inventNumber).isLossPowerFlag()){
                activityType = "(praca)";
            } else{
                activityType="(test)";
            }
        }

        if(device.getChillerSet()!=null)
            chillerSetDTO.setActualSetPoint(deviceDAO.findByInventNumber(inventNumber).getChillerSet().getActualSetPoint());

        if(device.getDrycoolerSet()!=null){
            drycoolerSetDTO.setActualSetPoint_CWL(device.getDrycoolerSet().getActualSetPoint_CWL());
            drycoolerSetDTO.setActualSetPoint_CWR(device.getDrycoolerSet().getActualSetPoint_CWR());
            drycoolerSetDTO.setActualSetPoint_AmbL(device.getDrycoolerSet().getActualSetPoint_AmbL());
            drycoolerSetDTO.setActualSetPoint_AmbR(device.getDrycoolerSet().getActualSetPoint_AmbR());
        }

        model.addAttribute("chillerSetDTO", chillerSetDTO);
        model.addAttribute("drycoolerSetDTO", drycoolerSetDTO);
        model.addAttribute("activityType", activityType);
        model.addAttribute("dto", deviceDTO);
        model.addAttribute("dao",device);
        model.addAttribute("issueDAO", issueDocumentDAO);
        model.addAttribute("CalUtil", new CalendarUtil());
        model.addAttribute("lastTest", lastTest);
        model.addAttribute("posts",posts);
        return "devices/deviceInfo";
    }

    @Override
    public String createDevice(DeviceDTO deviceDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;

            if(deviceDAO.existsById(deviceDTO.getInventNumber())) {
                FieldError fieldError = new FieldError("newDevice", "inventNumber", deviceDTO.getInventNumber(),
                        false, null, null, "Urządzenie o tym numerze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return addFormErr(model,deviceDTO.getRoom().name(),deviceDTO);
        }

        if(deviceDAO.existsById(deviceDTO.getInventNumber())){
            FieldError fieldError = new FieldError("newDevice","inventNumber", deviceDTO.getInventNumber(),false,null,null ,"Device with this Invent Number already exist in database!");

            bindingResult.addError(fieldError);
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",fieldError);
            model.addAttribute("errorsAmount",1);
            return addFormErr(model,deviceDTO.getRoom().name(),deviceDTO);
        }

        deviceDTO.setRoom(deviceDTO.getRoom());
        create(deviceDTO);
        return "redirect:/devices/"+ deviceDTO.getInventNumber() ;
    }

    @Override
    public String createDeviceInRoom(Model model, String room) {
        model.addAttribute("newDevice", new DeviceDTO());
        model.addAttribute("room", Room.valueOf(room));
        return "devices/addDeviceForm";
    }

    @Override
    public String createActivityForGenerator(Model model, String inventNumber) {

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        GeneratorTestDTO generatorTestDTO = new GeneratorTestDTO();

        generatorTestDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        generatorTestDTO.setContent(
                "\nUwagi: \n\n\n"+
                        "Czas trwania uruchomienia: \n" +
                        "Temp:  st.C\n" +
                        "Ciśnienie oleju: bar\n" +
                        "Stan paliwa: \n" +
                        "Napiecie aku: V\n" +
                        "Całkowity czas pracy: h\n"
        );
        model.addAttribute("text",text);
        model.addAttribute("newTest", generatorTestDTO);
        model.addAttribute("device", inventNumber);
        return "devices/addActForm";
    }


    @Override
    @Transactional
    public String setChillerTemp(ChillerSetDTO chillerSetDTO, BindingResult binding, Model model, String inventNumber) {
        Device chiller = deviceDAO.findByInventNumber(inventNumber);
        ChillerSet actualChillerSet = chiller.getChillerSet();
        ChillerSet saved = ChillerSetMapper.map(chillerSetDTO, deviceDAO);

        if(binding.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = binding.getFieldErrors();

            model.addAttribute("bindingResult", binding);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return findByInventNumberErr(inventNumber,model) ;
        }

        chillerSetDTO.setAuthor(Author.valueOf(MainController.getUser()));
        chillerSetDTO.setInventNumber(chiller.getInventNumber());
        chillerSetDTO.setSetDate(CalendarUtil.cal2string(Calendar.getInstance()));

        if(actualChillerSet!=null){
            chillerSetDTO.setPreviousAuthor(actualChillerSet.getAuthor());
            chillerSetDTO.setPreviousSetDate(CalendarUtil.cal2string(actualChillerSet.getSetDate()));
            chillerSetDTO.setPreviousSetPoint(actualChillerSet.getActualSetPoint());
        }
        else {
            chillerSetDTO.setPreviousAuthor(chillerSetDTO.getAuthor());
            chillerSetDTO.setPreviousSetDate(chillerSetDTO.getSetDate());
            chillerSetDTO.setPreviousSetPoint(chillerSetDTO.getActualSetPoint());
        }

        ShortPostDTO shortPostDTO = new ShortPostDTO(Author.DTN,"Zmieniono nastawę chillera! [SYSTEM]",CalendarUtil.cal2string(Calendar.getInstance()),inventNumber,false,PostLevel.INFO);
        postService.create(shortPostDTO);
        chillerSetDAO.save(saved);
        chiller.setChillerSet(saved);
        deviceDAO.save(chiller);
        if(actualChillerSet != null) chillerSetDAO.delete(actualChillerSet);
        return "redirect:/dtnetwork";
    }

    @Override
    public String setDrycoolerTemp(DrycoolerSetDTO drycoolerSetDTO, BindingResult binding, Model model, String inventNumber) {
        Device drycooler = deviceDAO.findByInventNumber(inventNumber);
        DrycoolerSet actualDrycoolerSet = drycooler.getDrycoolerSet();
        DrycoolerSet saved = DrycoolerSetMapper.map(drycoolerSetDTO, deviceDAO);

        if(binding.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = binding.getFieldErrors();
            model.addAttribute("bindingResult", binding);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return findByInventNumberErr(inventNumber,model) ;
        }

        drycoolerSetDTO.setAuthor(Author.valueOf(MainController.getUser()));
        drycoolerSetDTO.setInventNumber(drycooler.getInventNumber());
        drycoolerSetDTO.setSetDate(CalendarUtil.cal2string(Calendar.getInstance()));

        if(actualDrycoolerSet!=null){
            drycoolerSetDTO.setPreviousAuthor(actualDrycoolerSet.getAuthor());
            drycoolerSetDTO.setPreviousSetDate(CalendarUtil.cal2string(actualDrycoolerSet.getSetDate()));
            drycoolerSetDTO.setPreviousSetPoint_CWL(actualDrycoolerSet.getActualSetPoint_CWL());
            drycoolerSetDTO.setPreviousSetPoint_CWR(actualDrycoolerSet.getActualSetPoint_CWR());
            drycoolerSetDTO.setPreviousSetPoint_AmbL(actualDrycoolerSet.getActualSetPoint_AmbL());
            drycoolerSetDTO.setPreviousSetPoint_AmbR(actualDrycoolerSet.getActualSetPoint_AmbR());
        }
        else {
            drycoolerSetDTO.setPreviousAuthor(drycoolerSetDTO.getAuthor());
            drycoolerSetDTO.setPreviousSetDate(drycoolerSetDTO.getSetDate());
            drycoolerSetDTO.setPreviousSetPoint_CWL(drycoolerSetDTO.getActualSetPoint_CWL());
            drycoolerSetDTO.setPreviousSetPoint_CWR(drycoolerSetDTO.getActualSetPoint_CWR());
            drycoolerSetDTO.setPreviousSetPoint_AmbL(drycoolerSetDTO.getActualSetPoint_AmbL());
            drycoolerSetDTO.setPreviousSetPoint_AmbR(drycoolerSetDTO.getActualSetPoint_AmbR());
        }

        ShortPostDTO shortPostDTO = new ShortPostDTO(Author.DTN,"Zmieniono nastawę drycoolera! [SYSTEM]",CalendarUtil.cal2string(Calendar.getInstance()),inventNumber,false,PostLevel.INFO);
        postService.create(shortPostDTO);
        drycoolerSetDAO.save(saved);
        drycooler.setDrycoolerSet(saved);
        deviceDAO.save(drycooler);
        if(actualDrycoolerSet != null) drycoolerSetDAO.delete(actualDrycoolerSet);
        return "redirect:/dtnetwork";
    }

    @Override
    public String getAllGeneratorTests(String inv, Model model, int page) {
        List<GeneratorTestDTO> testPage = getAllGeneratorTests(page-1,pagesize,inv);
        DeviceDTO dto = generateMainViewForDevice(inv);
        int numberOfPages = (generatorTestDAO.findAllByDevice_InventNumber(inv).size())/pagesize+1;
        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

        if(generatorTestDAO.findAllByDevice_InventNumber(inv).size()%pagesize==0){
            numberOfPages --;
        }

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("tests",testPage);
        model.addAttribute("amount", generatorTestDAO.findAllByDevice_InventNumber(inv).size());
        model.addAttribute("dto", dto);
        return "devices/getAllTests";
    }

    public String findByInventNumberErr(String inventNumber, Model model){
        DeviceDTO deviceDTO = generateMainViewForDevice(inventNumber);
        Device device= deviceDAO.findByInventNumber(inventNumber);
        String lastTest = "Nieznana";
        String activityType = "(nieznane)";
        int posts = device.getShortPosts().size();

        if(device.getTests().size()!=0){
            lastTest = CalendarUtil.cal2string(generatorTestDAO.findTopByDevice_InventNumberOrderByDateDesc(inventNumber).getDate()) ;

            if(generatorTestDAO.findTopByDevice_InventNumberOrderByDateDesc(inventNumber).isLossPowerFlag()){
                activityType = "(praca)";
            } else{
                activityType="(test)";
            }
        }
        model.addAttribute("chillerSetDTO",new ChillerSetDTO());
        model.addAttribute("activityType", activityType);
        model.addAttribute("dto", deviceDTO);
        model.addAttribute("dao",device);
        model.addAttribute("issueDAO", issueDocumentDAO);
        model.addAttribute("CalUtil", new CalendarUtil());
        model.addAttribute("lastTest", lastTest);
        model.addAttribute("posts",posts);
        return "devices/deviceInfo";
    }



    public String addFormErr(Model model, @PathVariable String room, DeviceDTO deviceDTO){

        model.addAttribute("newDevice", deviceDTO);
        model.addAttribute("room", Room.valueOf(room));

        return "devices/addDeviceForm";
    }

    public List<GeneratorTestDTO> getAllGeneratorTests(int page, int size, String inv) {

        Page<GeneratorTest> page1 = generatorTestDAO.findByDevice_InventNumberOrderByDateDesc(inv, PageRequest.of(
                page, size, Sort.Direction.DESC,"date"));

        return page1.stream()
                .map(GeneratorTestMapper::map)
                .collect(Collectors.toList());
    }

}
