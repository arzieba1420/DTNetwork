package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.services.damage.DamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/damage")
public class DamageController {



    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;

    @Autowired
    public DamageController(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, DamageService damageService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.damageService = damageService;
    }

    @GetMapping("/getAll")
    public List<DamageDTO> findAll(){
        return damageService.findAll();
    }

    @PostMapping("/add")
    public DamageDTO add(@RequestBody DamageDTO damageDTO){
        return damageService.create(damageDTO);
    }

    @GetMapping("/ids/{id}")
    public DamageDTO findById(@PathVariable Integer id){
        return damageService.findById(id);
    }

    @PutMapping("/update") //requires damageID
    public DamageDTO update( DamageDTO damageDTO){
       return damageService.update(damageDTO);
    }

    @DeleteMapping("/delete/{id}")
    public DamageDTO remove(@PathVariable Integer id){
        return damageService.remove(id);
    }

}
