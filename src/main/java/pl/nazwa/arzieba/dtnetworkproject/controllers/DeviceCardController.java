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
import java.util.List;

@Controller
@RequestMapping("/cards")
public class DeviceCardController {
    private DeviceCardDAO deviceCardDAO;
    private DeviceDAO deviceDAO;
    private DeviceCardService deviceCardService;

    public DeviceCardController(DeviceCardDAO dao, DeviceDAO deviceDAO, DeviceCardService service) {
        this.deviceCardDAO = dao;
        this.deviceDAO = deviceDAO;
        this.deviceCardService = service;
    }

    //tests only
    @DeleteMapping("/delete")
    public String removeById(@RequestParam Integer id){
        deviceCardDAO.deleteById(id);
        return "Device card with id "+id+" has been removed";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addForm(Model model, @PathVariable String inventNumber){
        return deviceCardService.addForm(model,inventNumber);
    }

    @PostMapping("/addAsModel")
    public String create(Model model, @Valid @ModelAttribute("newCard") DeviceCardDTO dto, BindingResult bindingResult){
        return deviceCardService.create(model,dto,bindingResult);
    }
}
