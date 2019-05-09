package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedList;
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


    @GetMapping("/devices/{inventNumber}")
    public String findByInventNumber(@PathVariable String inventNumber, Model model){

        List<IssueDocumentDTO> docs = deviceService.getIssueDocuments(inventNumber);
        DeviceDTO dto = deviceService.findByInventNumber(inventNumber);
        model.addAttribute("docs", docs);
        model.addAttribute("dto", dto);

        return "devices/getAllDocs";
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
    public String  create3(Model model, @Valid @ModelAttribute("dto")  IssueDocumentDTO dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);

            model.addAttribute("errorsAmount", allErrors.size());
            return addFormDam(dto.getDamageId(), model);
        }
        issueDocService.create(dto);
        return "redirect:/devices/" + dto.getInventNumber();
    }

    @PostMapping("/addAsModel/stay2")
    public String  create(Model model, @Valid @ModelAttribute("newDoc")  IssueDocumentDTO dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);

            model.addAttribute("errorsAmount", allErrors.size());
            return addFormDev(dto.getInventNumber(), model);
        }
        issueDocService.create(dto);
        return "redirect:/devices/" + dto.getInventNumber();
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

    @GetMapping("/{year}/{page}")
    public String getAllForYear(@PathVariable int year, Model model,@PathVariable int page){

        List<IssueDocumentDTO> docs= issueDocService.findByYear(year,page-1,10);
        int numberOfPages = (issueDocService.numberByYear(year))/10 + 1;

        if(issueDocService.numberByYear(year)%10==0){
            numberOfPages--;
        }
        List<Integer> morePages = new LinkedList<>();

        int i = 2;
        int lastPage = 1;

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("year",year);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("docs",docs);
        return "documents/getAllForYear";
    }




}
