package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import com.arzieba.dtnetworkproject.services.deviceCard.DeviceCardService;
import com.arzieba.dtnetworkproject.services.deviceCard.DeviceCardServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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


    @GetMapping("/all")
    public List<DeviceCardDTO> findAll(){
        return service.findAll();
    }

    @GetMapping("/signatures")
    public List<String> showSignatures(){
        return service.showAllSignatures();
    }

    @GetMapping("/devices/{inv}")
    public DeviceCardDTO findByDevice(@PathVariable String inv){
        return service.findForDevice(inv);
    }

    @GetMapping("/signatures/{sign}")
    public DeviceCardDTO findBySignature(@PathVariable String sign){
        return service.findForSignature(sign);
    }

    @GetMapping("/{id}")
    public DeviceCardDTO findById(@PathVariable Integer id){
        return service.findForId(id);
    }

    @PostMapping("/add")
    public String create(@RequestBody DeviceCardDTO dto){
        return service.create(dto);
    }


    //tests only
    @DeleteMapping("/delete")
    public String removeById(@RequestParam Integer id){
        dao.deleteById(id);
        return "Device card with id "+id+" has been removed";

    }

}
