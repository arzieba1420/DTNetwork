package pl.nazwa.arzieba.dtnetworkproject.controllers;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.model.Status;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.generator.GeneratorService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.model.Room;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("devices")
public class DeviceController {

    @Autowired
    public DeviceController(DeviceDAO deviceDAO,
                            DamageDAO damageDAO,
                            IssueDocumentDAO issueDocumentDAO,
                            DeviceCardDAO deviceCardDAO,
                            DeviceService deviceService, GeneratorTestDAO generatorTestDAO, GeneratorService generatorService, MainController mainController, ShortPostService postService, DamageService damageService, DamageController damageController) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.deviceService = deviceService;
        this.generatorTestDAO = generatorTestDAO;
        this.generatorService = generatorService;
        this.mainController = mainController;
        this.postService = postService;
        this.damageService = damageService;
        this.damageController = damageController;
    }

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DeviceService deviceService;
    private GeneratorTestDAO generatorTestDAO;
    private GeneratorService generatorService;
    private MainController mainController;
    private ShortPostService postService;
    private DamageService damageService;
    private DamageController damageController;



    //Returns DeviceDTO by ID = inventNumber
    @GetMapping("/{inventNumber}")
    public String findByInventNumber(@PathVariable String inventNumber, Model model){
         DeviceDTO dto= deviceService.findByInventNumber(inventNumber);
         Device device= deviceDAO.findByInventNumber(inventNumber);

         String lastTest = "Nieznana";
        String activityType = "(nieznane)";

         if(device.getTests().size()!=0){
             lastTest = CalendarUtil.cal2string(generatorTestDAO.findTopByDevice_InventNumberOrderByDateDesc(inventNumber).getDate()) ;

             if(generatorTestDAO.findTopByDevice_InventNumberOrderByDateDesc(inventNumber).isLossPowerFlag()){
                 activityType = "(praca)";
             } else{
                 activityType="(test)";
             }
         }




         model.addAttribute("activityType", activityType);
         model.addAttribute("dto",dto);
         model.addAttribute("dao",device);
         model.addAttribute("issueDAO", issueDocumentDAO);
         model.addAttribute("CalUtil", new CalendarUtil());
         model.addAttribute("lastTest", lastTest);
         int posts = device.getShortPosts().size();
        model.addAttribute("posts",posts);
         return "devices/deviceInfo";
    }


    @PostMapping("/addAsModel")
    public String create2(Model model, @Valid @ModelAttribute("newDevice") DeviceDTO dto, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;

            if(deviceDAO.existsById(dto.getInventNumber())) {

                FieldError fieldError = new FieldError("newDevice", "inventNumber", dto.getInventNumber(),
                        false, null, null, "Urządzenie o tym numerze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return addFormErr(model,dto.getRoom().name(),dto);
        }

        if(deviceDAO.existsById(dto.getInventNumber())){

            FieldError fieldError = new FieldError("newDevice","inventNumber", dto.getInventNumber(),false,null,null ,"Device with this Invent Number already exist in database!");
            bindingResult.addError(fieldError);
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",fieldError);
            model.addAttribute("errorsAmount",1);
            return addFormErr(model,dto.getRoom().name(),dto);
    }
        dto.setRoom(dto.getRoom());
        deviceService.create(dto);
        return "redirect:/devices/"+ dto.getInventNumber() ;
    }

    @GetMapping("/addForm/{room}")
    public String addForm(Model model, @PathVariable String room){
        model.addAttribute("newDevice", new DeviceDTO());
        model.addAttribute("room", Room.valueOf(room));
        return "devices/addDeviceForm";
    }

    public String addFormErr(Model model, @PathVariable String room, DeviceDTO deviceDTO){
        model.addAttribute("newDevice", deviceDTO);
        model.addAttribute("room", Room.valueOf(room));
        return "devices/addDeviceForm";
    }

    @GetMapping("/home")
    public String home(){
        return "index.html";
    }

    @GetMapping("/activityForm/{inv}")
    public String addActForm(Model model, @PathVariable String inv){

        String text = deviceDAO.findByInventNumber(inv).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inv).getRoom();
        model.addAttribute("text",text);

        model.addAttribute("newTest", new GeneratorTestDTO());
        model.addAttribute("device", inv);
        return "devices/addActForm";
    }


    public String addActFormErr(Model model, String inv, GeneratorTestDTO testDTO ){

        model.addAttribute("newTest", testDTO);
        model.addAttribute("device", inv);
        return "devices/addActForm";
    }

    @PostMapping("/addTest")
    public String addTest( Model model, @Valid @ModelAttribute("newTest") GeneratorTestDTO testDTO,  BindingResult bindingResult ){


        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return addActFormErr(model, testDTO.getInventNumber(), testDTO );
        }

        if( testDTO.isLossPowerFlag()==false && testDTO.getStatus()!= Status.DAMAGE ) {
            generatorService.create(testDTO);
            return "redirect:/generators/" + testDTO.getInventNumber()+"/1";
        }
        if(testDTO.isLossPowerFlag()==true && testDTO.getStatus()!= Status.DAMAGE){

            ShortPostDTO postDTO = new ShortPostDTO();
            postDTO.setDate(testDTO.getDate());
            postDTO.setAuthor(Author.valueOf(mainController.getUser()));
            postDTO.setInventNumber(testDTO.getInventNumber());
            postDTO.setContent("Generator podał napięcie podczas zaniku! [SYSTEM]");
            generatorService.create(testDTO);
            postService.create(postDTO);
            return "redirect:/generators/" + testDTO.getInventNumber()+"/1";
        }
        if(testDTO.isLossPowerFlag()==false && testDTO.getStatus()== Status.DAMAGE){
            DamageDTO damageDTO = new DamageDTO();
            damageDTO.setDescription(testDTO.getContent());
            damageDTO.setDeviceInventNumber(testDTO.getInventNumber());
            damageDTO.setAuthor(Author.valueOf(mainController.getUser()));
            damageDTO.setDamageDate(testDTO.getDate());
            damageDTO.setNewPostFlag(true);
            ShortPostDTO dto = new ShortPostDTO();
            dto.setDate(damageDTO.getDamageDate());
            dto.setAuthor(damageDTO.getAuthor());
            dto.setInventNumber(damageDTO.getDeviceInventNumber());
            dto.setContent("Nowa usterka! Szczegóły po kliknięciu w Urządzenie... [SYSTEM]");
            dto.setForDamage(true);
            generatorService.create(testDTO);
            damageService.create(damageDTO);
            postService.create(dto);
            return "redirect:/dtnetwork";
        }

        if(testDTO.isLossPowerFlag()==true && testDTO.getStatus()== Status.DAMAGE){



            ShortPostDTO postDTO = new ShortPostDTO();
            postDTO.setDate(testDTO.getDate());
            postDTO.setAuthor(Author.valueOf(mainController.getUser()));
            postDTO.setInventNumber(testDTO.getInventNumber());
            postDTO.setContent("Generator podał napięcie podczas zaniku! [SYSTEM]");
            DamageDTO damageDTO = new DamageDTO();
            damageDTO.setDescription(testDTO.getContent());
            damageDTO.setDeviceInventNumber(testDTO.getInventNumber());
            damageDTO.setAuthor(Author.valueOf(mainController.getUser()));
            damageDTO.setDamageDate(testDTO.getDate());
            damageDTO.setNewPostFlag(true);
            ShortPostDTO dto = new ShortPostDTO();
            dto.setDate(damageDTO.getDamageDate());
            dto.setAuthor(damageDTO.getAuthor());
            dto.setInventNumber(damageDTO.getDeviceInventNumber());
            dto.setContent("Nowa usterka! Szczegóły po kliknięciu w Urządzenie... [SYSTEM]");
            dto.setForDamage(true);
            generatorService.create(testDTO);
            postService.create(postDTO);
            damageService.create(damageDTO);
            postService.create(dto);

            return "redirect:/dtnetwork";
        }

        return "redirect:/error";
    }

}
