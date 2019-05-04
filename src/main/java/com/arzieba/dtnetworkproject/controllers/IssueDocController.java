package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import com.arzieba.dtnetworkproject.services.damage.DamageService;
import com.arzieba.dtnetworkproject.services.device.DeviceService;
import com.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/issueDocs")
public class IssueDocController {

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private IssueDocService issueDocService;
    private DeviceService deviceService;

    @Autowired
    public IssueDocController(DamageService damageService, DeviceService deviceService, DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, IssueDocService issueDocService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.issueDocService = issueDocService;
        this.deviceService=deviceService;
        this.damageService=damageService;
    }

    @GetMapping("/all")
    public List<IssueDocumentDTO> findAll(){
        return issueDocService.findAll();
    }

    @GetMapping("/signatures/{signature}")
    public IssueDocumentDTO findBySignature(@PathVariable String signature){
        return issueDocService.findBySignature(signature);
    }

    @GetMapping("/devices/{inventNumber}")
    public String findByInventNumber(@PathVariable String inventNumber, Model model){

        List<IssueDocumentDTO> docs = deviceService.getIssueDocuments(inventNumber);
        DeviceDTO dto = deviceService.findByInventNumber(inventNumber);
        model.addAttribute("docs", docs);
        model.addAttribute("dto", dto);

        return "devices/getAllDocs";
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

    @GetMapping("/years")
    public String getYears(){
        issueDocService.setOfYears();
        return "OK";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addFormDev(@PathVariable String inventNumber, Model model){
        model.addAttribute("newDoc", new IssueDocumentDTO());
        model.addAttribute("inventNumber", inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);

        return "documents/addDocFormDev";
    }

    @GetMapping("/addFormDam/{damageId}")
    public String addFormDam(@PathVariable Integer damageId, Model model){
        model.addAttribute("newDoc", new IssueDocumentDTO());
        String inventNumber = damageDAO.findById(damageId).orElse(null).getDevice().getInventNumber();
        model.addAttribute("inventNumber", inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);
        model.addAttribute("damageId",damageId);

        return "documents/addDocFormDam";
    }

    @PostMapping("/addAsModel/stay")
    public String  create3(Model model, @ModelAttribute("dto")  IssueDocumentDTO dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "posts/addPostForm";
        }
        issueDocService.create(dto);
        return "redirect:/devices/"+dto.getInventNumber();
    }

    @GetMapping("/damages/{damageId}")
    public String getAllForDamage(@PathVariable Integer damageId, Model model){
        String inventNumber = damageService.findById(damageId).getDeviceInventNumber();
        DeviceDTO dto= deviceService.findByInventNumber(inventNumber) ;
        List<IssueDocumentDTO> docs= issueDocService.findByDamageId(damageId);
        model.addAttribute("docs",docs);
        model.addAttribute("dto",dto);
        model.addAttribute("damageId", damageId);
        return "damages/getAllDocs";

    }

    @GetMapping("/{year}")
    public String getAllForYear(@PathVariable int year, Model model){
        List<IssueDocumentDTO> docs= issueDocService.findByYear(year);
        model.addAttribute("year", year);
        model.addAttribute("docs",docs);
        return "documents/getAllForYear";
    }




}
