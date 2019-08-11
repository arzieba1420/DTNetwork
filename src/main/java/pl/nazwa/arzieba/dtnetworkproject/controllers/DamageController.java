package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/damages")
public class DamageController {



    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private ShortPostService postService;
    private DeviceService deviceService;
    private IssueDocService issueDocService;


    @Autowired
    public DamageController(DeviceService deviceService, ShortPostService postService, DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, DamageService damageService, IssueDocService issueDocService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.damageService = damageService;
        this.postService = postService;
        this.deviceService=deviceService;
        this.issueDocService = issueDocService;
    }



    @PostMapping("/addAsModel")
    public String add(@Valid @ModelAttribute("newDamage") DamageDTO damageDTO, BindingResult bindingResult,Model model){

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();


            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);

            model.addAttribute("errorsAmount",allErrors.size());
            return createDamageErr(model,damageDTO.getDeviceInventNumber(),damageDTO);
        }

        if(!damageDTO.isNewPostFlag()) {
            damageService.create(damageDTO);

        } else{
            ShortPostDTO dto = new ShortPostDTO();
            dto.setDate(damageDTO.getDamageDate());
            dto.setAuthor(Author.DTN);
            dto.setInventNumber(damageDTO.getDeviceInventNumber());
            dto.setContent("Nowa usterka! Szczegóły po kliknięciu w Urządzenie... [SYSTEM]");
            dto.setForDamage(true);
            damageService.create(damageDTO);
            postService.create(dto);

        }
        return "redirect:/damages/devices/"+damageDTO.getDeviceInventNumber()+"/1";
    }

    @GetMapping("/devices/{inventNumber}/{page}")
    public String getAllForDevice(@PathVariable String inventNumber, Model model, @PathVariable int page){
        List<Damage> damages = damageService.findByDeviceInventNumber(page-1,10,inventNumber);
        DeviceDTO dto = deviceService.findByInventNumber(inventNumber);


        Map<Integer,DamageDTO> mapa = new LinkedHashMap<>();
        for (Damage damage: damages) {
            mapa.put(damage.getDamageId(), DamageMapper.map(damage));
        }

        int numberOfPages = (damageService.numberOfDamagesByDevice(inventNumber))/10 + 1;

        if(damageService.numberOfDamagesByDevice(inventNumber)%10==0){
            numberOfPages--;
        }
        List<Integer> morePages = new LinkedList<>();

        int i = 2;
        int lastPage = 1;

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("amount", damageService.numberOfDamagesByDevice(inventNumber));
        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("inventNumber",inventNumber);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("damages",mapa);
        model.addAttribute("dto",dto);

        return "damages/allDamagesForDevice";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String createDamage(Model model,@PathVariable String inventNumber){

        model.addAttribute("newDamage", new DamageDTO());
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);

        return "damages/addForm";
    }

    public String createDamageErr(Model model,@PathVariable String inventNumber, DamageDTO damageDTO){

        model.addAttribute("newDamage", damageDTO);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);

        return "damages/addForm";
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

    @GetMapping("/edit/{id}")
    public String editDamage(Model model, @PathVariable Integer id){
        DamageDTO damageDTO = damageService.findById(id);
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

    @PostMapping("/edit/{id}")
    public String saveEditedDamage(@Valid @ModelAttribute("newDamage") DamageDTO damageDTO, BindingResult bindingResult,@PathVariable Integer id,Model model){

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

        return "redirect:/damages/devices/"+damageDTO.getDeviceInventNumber()+"/1";
    }

    public int amountOfDocsForDamage(int damageId){

        return issueDocService.findByDamageId(damageId).size();
    }

}
