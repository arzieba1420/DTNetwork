package com.arzieba.dtnetworkproject.controllers;


import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.Device;
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
    public DeviceController(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
    }

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;

    @GetMapping(value="/get", produces = "application/json")

    public List<String> all(){
        List<String> names = new ArrayList<>();
        int i = deviceDAO.findAll().size();
        for (Device device:deviceDAO.findAll() ) {
            names.add(device.getInventNumber());
        }

        return names;
    }

    @GetMapping("getById")
    public ResponseEntity<DeviceDTO> getByID(@RequestParam String id){

        return ResponseEntity.ok(Device.mapper(deviceDAO.findByInventNumber(id)));
    }

    @GetMapping("getByDesc")
    public ResponseEntity<DeviceDTO> getByDesc(@RequestParam String string){
        Device found = deviceDAO.findByDeviceDescription(string);
        return ResponseEntity.ok(Device.mapper(found));

    }

    @PostMapping(value="add", produces = "application/json")
    public ResponseEntity<DeviceDTO> add(@RequestBody DeviceDTO deviceDTO){

        deviceDAO.save(DeviceDTO.mapper(deviceDTO));

        return ResponseEntity.ok(deviceDTO);
    }

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
}
