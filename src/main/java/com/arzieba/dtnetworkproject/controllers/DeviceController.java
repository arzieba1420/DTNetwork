package com.arzieba.dtnetworkproject.controllers;


import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import com.arzieba.dtnetworkproject.model.*;
import com.arzieba.dtnetworkproject.services.device.DeviceService;

import com.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

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


    //Returns list of all Devices from DB as DTO's
    @GetMapping(value="/getAll", produces = "application/json")
    public List<DeviceDTO> findAll(){
        return deviceService.findAll();
    }

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

    @GetMapping("/types/{type}")
    public List<DeviceDTO> findByType(@PathVariable String type){
        return deviceService.findByType(type);
    }

    @PostMapping("/add")
    public DeviceDTO create(@RequestBody DeviceDTO deviceDTO){
        return deviceService.create(deviceDTO);
    }

    @PostMapping("/addAsModel")
    public String create2(Model model, @ModelAttribute("dto") DeviceDTO dto ){
        dto.setRoom(dto.getRoom());
        deviceService.create(dto);
        return "redirect:/devices/"+ dto.getInventNumber() ;
    }

    @GetMapping("/addForm/{room}")
    public String addForm(Model model, @PathVariable String room){
        model.addAttribute("newDevice", new DeviceDTO());
        model.addAttribute("room",Room.valueOf(room));
        return "devices/addDeviceForm";
    }

    @PutMapping("/update")
    public DeviceDTO update(@RequestBody DeviceDTO deviceDTO){
        return deviceService.update(deviceDTO);
    }

    @DeleteMapping("/remove")
    public DeviceDTO remove(String inventNumber){
        return deviceService.remove(inventNumber);
    }

    @GetMapping("/damages/{inventNumber}")
    public List<DamageDTO> getDamages(@PathVariable String inventNumber){
        return deviceService.getDamages(inventNumber);
    }

    @GetMapping("/issues/{inv}")
    public List<IssueDocumentDTO> getIssueDocs(@PathVariable String inv){
        return deviceService.getIssueDocuments(inv);
    }

    @GetMapping("/cards/{inv}")
    public DeviceCardDTO getDeviceCard(@PathVariable String inv){
        return deviceService.getDeviceCard(inv);
    }



    @GetMapping("/home")
    public String home(){
        return "index.html";
    }





}
