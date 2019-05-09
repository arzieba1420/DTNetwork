package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.deviceCard.DeviceCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cards")
public class DeviceCardController {

    private DeviceCardDAO dao;
    private DeviceDAO deviceDAO;
    private DeviceCardService service;

    public DeviceCardController(DeviceCardDAO dao, DeviceDAO deviceDAO, DeviceCardService service) {
        this.dao = dao;
        this.deviceDAO = deviceDAO;
        this.service = service;
    }

    //tests only
    @DeleteMapping("/delete")
    public String removeById(@RequestParam Integer id){
        dao.deleteById(id);
        return "Device card with id "+id+" has been removed";

    }

    @GetMapping("/addForm/{inventNumber}")
    public String addForm(Model model, @PathVariable String inventNumber){
        model.addAttribute("newCard", new DeviceCardDTO());
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("room", deviceDAO.findByInventNumber(inventNumber).getRoom());
        model.addAttribute("type", deviceDAO.findByInventNumber(inventNumber).getDeviceType());
        return "devices/addCardForm";
    }

    @PostMapping("/addAsModel")
    public String create(Model model, @Valid @ModelAttribute("newCard") DeviceCardDTO dto, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());


            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);


            model.addAttribute("errorsAmount",allErrors.size());
            return addForm(model,dto.getInventNumber());
        }

        service.create(dto);
        return "redirect:/devices/"+dto.getInventNumber();
    }

}
