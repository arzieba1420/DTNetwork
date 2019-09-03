package pl.nazwa.arzieba.dtnetworkproject.controllers;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.*;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.generator.GeneratorService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.utils.chillerSet.ChillerSetMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.drycoolerSet.DrycoolerSetMapper;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;


@Controller
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    public DeviceController(DeviceDAO deviceDAO,
                            DamageDAO damageDAO,
                            IssueDocumentDAO issueDocumentDAO,
                            DeviceCardDAO deviceCardDAO,
                            DeviceService deviceService, GeneratorTestDAO generatorTestDAO, GeneratorService generatorService, MainController mainController, ShortPostService postService, DamageService damageService, DamageController damageController, UserDAO userDAO, ChillerSetDAO chillerSetDAO, DrycoolerSetDAO drycoolerSetDAO) {
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
        this.userDAO = userDAO;
        this.chillerSetDAO = chillerSetDAO;
        this.drycoolerSetDAO = drycoolerSetDAO;
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
    private UserDAO userDAO;
    private ChillerSetDAO chillerSetDAO;
    private DrycoolerSetDAO drycoolerSetDAO;



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


         ChillerSetDTO chillerSetDTO = new ChillerSetDTO();
         if(device.getChillerSet()!=null)
         chillerSetDTO.setActualSetPoint(deviceDAO.findByInventNumber(inventNumber).getChillerSet().getActualSetPoint());

         DrycoolerSetDTO drycoolerSetDTO = new DrycoolerSetDTO();
         if(device.getDrycoolerSet()!=null){
             drycoolerSetDTO.setActualSetPoint_CWL(device.getDrycoolerSet().getActualSetPoint_CWL());
             drycoolerSetDTO.setActualSetPoint_CWR(device.getDrycoolerSet().getActualSetPoint_CWR());
             drycoolerSetDTO.setActualSetPoint_AmbL(device.getDrycoolerSet().getActualSetPoint_AmbL());
             drycoolerSetDTO.setActualSetPoint_AmbR(device.getDrycoolerSet().getActualSetPoint_AmbR());
         }

         model.addAttribute("chillerSetDTO", chillerSetDTO);
         model.addAttribute("drycoolerSetDTO", drycoolerSetDTO);
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

    public String findByInventNumberErr(String inventNumber, Model model){
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



        model.addAttribute("chillerSetDTO",new ChillerSetDTO());
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
    public String create2(@Valid @ModelAttribute("newDevice") DeviceDTO dto, BindingResult bindingResult,Model model){

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
        return "redirect:/dtnetwork";
    }

    @GetMapping("/activityForm/{inv}")
    public String addActForm(Model model, @PathVariable String inv){

        String text = deviceDAO.findByInventNumber(inv).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inv).getRoom();
        GeneratorTestDTO generatorTestDTO = new GeneratorTestDTO();
    generatorTestDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        generatorTestDTO.setContent(
                "\nUwagi: \n\n\n"+
                "Czas trwania uruchomienia: \n" +
                "Temp:  st.C\n" +
                "Ciśnienie oleju: bar\n" +
                "Stan paliwa: \n" +
                "Napiecie aku: V\n" +
                "Całkowity czas pracy: h\n"+
                "\n\nSporządził: ");

        model.addAttribute("text",text);
        model.addAttribute("newTest", generatorTestDTO);
        model.addAttribute("device", inv);
        return "devices/addActForm";
    }


    public String addActFormErr(Model model, String inv, GeneratorTestDTO testDTO ){

        model.addAttribute("newTest", testDTO);
        model.addAttribute("device", inv);
        model.addAttribute("text",deviceDAO.findByInventNumber(inv).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inv).getRoom() );
        return "devices/addActForm";
    }

    @PostMapping("/addTest")
    public String addTest( @Valid @ModelAttribute("newTest") GeneratorTestDTO testDTO,  BindingResult bindingResult,Model model ){


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
            postDTO.setAuthor(Author.DTN);
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
            dto.setAuthor(Author.DTN);
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
            postDTO.setAuthor(Author.DTN);
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
            dto.setAuthor(Author.DTN);
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

    @PostMapping("/chillerSet/{inventNumber}")
    public String setChillerTemp( @ModelAttribute("chillerSetDTO") @Valid ChillerSetDTO chillerSetDTO, BindingResult binding, Model model, @PathVariable String inventNumber){

        if(binding.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = binding.getFieldErrors();
            model.addAttribute("bindingResult", binding);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return findByInventNumberErr(inventNumber,model) ;
        }


        Device chiller = deviceDAO.findByInventNumber(inventNumber);
        ChillerSet actualChillerSet = chiller.getChillerSet();
        ChillerSetDTO fromForm = chillerSetDTO;
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

        ShortPostDTO shortPostDTO = new ShortPostDTO();
        shortPostDTO.setContent("Zmieniono nastawę chillera! [SYSTEM]");
        shortPostDTO.setAuthor(Author.DTN);
        shortPostDTO.setInventNumber(inventNumber);
        shortPostDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        shortPostDTO.setForDamage(false);
        postService.create(shortPostDTO);

        ChillerSet saved =ChillerSetMapper.map(chillerSetDTO, deviceDAO);
        chillerSetDAO.save(saved);
        chiller.setChillerSet(saved);
        deviceDAO.save(chiller);

        if(actualChillerSet != null)
        chillerSetDAO.delete(actualChillerSet);



        return "redirect:/dtnetwork";
        }

    @PostMapping("/drycoolerSet/{inventNumber}")
    public String setDrycoolerTemp( @ModelAttribute("drycoolerSetDTO") @Valid DrycoolerSetDTO drycoolerSetDTO, BindingResult binding, Model model, @PathVariable String inventNumber){

        if(binding.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = binding.getFieldErrors();
            model.addAttribute("bindingResult", binding);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return findByInventNumberErr(inventNumber,model) ;
        }


        Device drycooler = deviceDAO.findByInventNumber(inventNumber);
        DrycoolerSet actualDrycoolerSet = drycooler.getDrycoolerSet();
        DrycoolerSetDTO fromForm = drycoolerSetDTO;
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

        ShortPostDTO shortPostDTO = new ShortPostDTO();
        shortPostDTO.setContent("Zmieniono nastawę drycoolera! [SYSTEM]");
        shortPostDTO.setAuthor(Author.DTN);
        shortPostDTO.setInventNumber(inventNumber);
        shortPostDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        shortPostDTO.setForDamage(false);
        postService.create(shortPostDTO);

        DrycoolerSet saved = DrycoolerSetMapper.map(drycoolerSetDTO, deviceDAO);
        drycoolerSetDAO.save(saved);
        drycooler.setDrycoolerSet(saved);
        deviceDAO.save(drycooler);

        if(actualDrycoolerSet != null)
            drycoolerSetDAO.delete(actualDrycoolerSet);



        return "redirect:/dtnetwork";
    }





}
