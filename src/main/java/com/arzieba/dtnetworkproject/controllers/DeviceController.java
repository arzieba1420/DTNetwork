package com.arzieba.dtnetworkproject.controllers;


import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.services.device.DeviceService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    public DeviceController(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, DeviceService deviceService) {
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

 /*   @GetMapping("getById")
    public ResponseEntity<DeviceDTO> getByID(@RequestParam String id){

        return ResponseEntity.ok(Device.mapper(deviceDAO.findByInventNumber(id)));
    }

    @GetMapping("getByName")
    public ResponseEntity<DeviceDTO> getByDesc(@RequestParam String string){
        Device found = deviceDAO.findByDeviceDescription(string);
        return ResponseEntity.ok(Device.mapper(found));

    }

    @PostMapping(value="add", produces = "application/json")
    public ResponseEntity<DeviceDTO> add(@RequestBody DeviceDTO deviceDTO){

        DeviceCard deviceCard = new DeviceCard();
        deviceCard.setAddress("Test adress");
        Device created = DeviceDTO.mapper(deviceDTO);
        deviceCard.setDevice(created);
        deviceDAO.save(created);
        deviceCardDAO.save(deviceCard);


        return ResponseEntity.ok(deviceDTO);
    }*/

    @GetMapping("/getDamages")
    public List<String> getDamages(@RequestParam String inventNumber){

        if (deviceDAO.existsById(inventNumber)) {

            List<Damage> damages = deviceDAO.findByInventNumber(inventNumber).getDamageList();
            List<String> descriptions = new ArrayList<>();
            for (Damage damage : damages) {
                descriptions.add(damage.getDescription());
            }
            return descriptions;
        } else {
            List<String> nieMa = new ArrayList<>();
            nieMa.add("Nie ma takiego urządzenia");
            return nieMa;
        }

    }


    //------------DO NOT USE YET!!!! TODO: CHANGE STRATEGY OF UPDATING

/*    @PutMapping("/update")
    public  ResponseEntity updateDevice(@RequestParam String inventNumber, @RequestBody DeviceDTO newDataDTO){







    }*/
}
