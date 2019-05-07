package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import com.arzieba.dtnetworkproject.services.deviceCard.DeviceCardService;
import com.arzieba.dtnetworkproject.services.deviceCard.DeviceCardServiceImpl;
import com.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String create(@ModelAttribute("dto") DeviceCardDTO dto){
        service.create(dto);
        return "redirect:/devices/"+dto.getInventNumber();
    }

}
