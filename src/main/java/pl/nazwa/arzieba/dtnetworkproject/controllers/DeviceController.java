package pl.nazwa.arzieba.dtnetworkproject.controllers;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.model.Room;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;

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
                            DeviceService deviceService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.deviceService = deviceService;
    }

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DeviceService deviceService;



    //Returns DeviceDTO by ID = inventNumber
    @GetMapping("/{inventNumber}")
    public String findByInventNumber(@PathVariable String inventNumber, Model model){
         DeviceDTO dto= deviceService.findByInventNumber(inventNumber);
         Device device= deviceDAO.findByInventNumber(inventNumber);
         model.addAttribute("dto",dto);
         model.addAttribute("dao",device);
         model.addAttribute("issueDAO", issueDocumentDAO);
         model.addAttribute("CalUtil", new CalendarUtil());
         int posts = device.getShortPosts().size();
        for (ShortPost post: device.getShortPosts()) {
            if (post.getContent().contains("damage")){
                posts--;
            }
        }
        model.addAttribute("posts",posts);
         return "devices/deviceInfo";
    }


    @PostMapping("/addAsModel")
    public String create2(Model model, @Valid @ModelAttribute("newDevice") DeviceDTO dto, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;

            if(deviceDAO.existsById(dto.getInventNumber())) {

                FieldError fieldError = new FieldError("newDevice", "inventNumber", dto.getInventNumber(),
                        false, null, null, "Device with this Invent Number already exist in database!");
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





}
