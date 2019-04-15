package com.arzieba.dtnetworkproject.controllers;


import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.services.device.DeviceService;

import com.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
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

    @GetMapping("/inventNumbers/{inventNumber}")
    public DeviceDTO findByInventNumber(@PathVariable String inventNumber){
        return deviceService.findByInventNumber(inventNumber);
    }

    @GetMapping("/types/{type}")
    public List<DeviceDTO> findByType(@PathVariable String type){
        return deviceService.findByType(type);
    }

    @PostMapping("/add")
    public DeviceDTO create(@RequestBody DeviceDTO deviceDTO){
        return deviceService.create(deviceDTO);
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





}
