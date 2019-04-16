package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import com.arzieba.dtnetworkproject.services.damage.DamageService;
import com.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issueDoc")
public class IssueDocController {

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private IssueDocService issueDocService;

    @Autowired
    public IssueDocController(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, IssueDocService issueDocService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.issueDocService = issueDocService;
    }

    @GetMapping("/getAll")
    public List<IssueDocumentDTO> findAll(){
        return issueDocService.findAll();
    }

    @GetMapping("/signatures/{signature}")
    public IssueDocumentDTO findBySignature(@PathVariable String signature){
        return issueDocService.findBySignature(signature);
    }

    @GetMapping("/devices/{number}")
    public List<IssueDocumentDTO> findByInventNumber(@PathVariable String number){
        return issueDocService.findByInventNumber(number);
    }

    @GetMapping("/damages/{id}")
    public List<IssueDocumentDTO> findByDamageId(@PathVariable Integer id){
        return issueDocService.findByDamageId(id);
    }

    @PostMapping("/add")
    public IssueDocumentDTO create(IssueDocumentDTO documentDTO){
        return issueDocService.create(documentDTO);
    }

    @PutMapping("/update")
    public IssueDocumentDTO update(IssueDocumentDTO documentDTO){
        return issueDocService.update(documentDTO);
    }

    @DeleteMapping("/delete/{sign}")
    public IssueDocumentDTO delete(@PathVariable String sign){
        return issueDocService.remove(sign);
    }




}
