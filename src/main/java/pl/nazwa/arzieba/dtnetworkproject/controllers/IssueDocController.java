package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

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

    @Value("${my.pagesize}")
    int pagesize;

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


    @GetMapping("/devices/{inventNumber}/{page}")
    public String findByInventNumber(@PathVariable String inventNumber, Model model, @PathVariable int page){

        List<IssueDocumentDTO> docs = deviceService.getIssueDocuments(inventNumber);
        DeviceDTO dto = deviceService.findByInventNumber(inventNumber);

        List<IssueDocumentDTO> page1= issueDocService.findByDevice(inventNumber,page-1,pagesize);

        List<String> numbers = docs.stream().map(d->d.getInventNumber()).collect(Collectors.toList());


        int numberOfPages = (issueDocService.numberByDevice(inventNumber))/pagesize + 1;

        if(issueDocService.numberByDevice(inventNumber)%pagesize==0){
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


        model.addAttribute("numbers",numbers);
        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("docs",page1);
        model.addAttribute("amount", docs.size());
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
    public String  create3(Model model, @Valid @ModelAttribute("newDoc")  IssueDocumentDTO issueDocumentDTO, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;

            if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
                FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }

            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormDamErr(issueDocumentDTO.getDamageId(), model,issueDocumentDTO);
        }

        if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
            FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
            bindingResult.addError(fieldError);

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", fieldError);
            model.addAttribute("errorsAmount", 1);
            return addFormDamErr(issueDocumentDTO.getDamageId(), model,issueDocumentDTO);

        }

        issueDocService.create(issueDocumentDTO);
        return "redirect:/devices/" + issueDocumentDTO.getInventNumber();
    }

    @PostMapping("/addAsModel/stay2")
    public String  create(Model model, @Valid @ModelAttribute("newDoc")  IssueDocumentDTO dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;

            if(issueDocumentDAO.existsByIssueSignature(dto.getIssueSignature())){
                FieldError fieldError = new FieldError("newDoc","issueSignature", dto.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }

            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormDevErr(dto.getInventNumber(), model,dto);
        }

        if(issueDocumentDAO.existsByIssueSignature(dto.getIssueSignature())){
            FieldError fieldError = new FieldError("newDoc","issueSignature", dto.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
            bindingResult.addError(fieldError);

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", fieldError);
            model.addAttribute("errorsAmount", 1);
            return addFormDevErr(dto.getInventNumber(), model,dto);

        }

        issueDocService.create(dto);
        return "redirect:/devices/" + dto.getInventNumber();
    }

    @GetMapping("/damages/{damageId}")
    public String getAllForDamage(@PathVariable Integer damageId, Model model){
        String inventNumber = damageService.findById(damageId).getDeviceInventNumber();
        DeviceDTO dto= deviceService.findByInventNumber(inventNumber) ;
        List<IssueDocumentDTO> docs= issueDocService.findByDamageId(damageId);

        model.addAttribute("amount", docs.size());
        model.addAttribute("docs",docs);
        model.addAttribute("dto",dto);
        model.addAttribute("damageId", damageId);
        return "damages/getAllDocs";

    }

    @GetMapping("/{year}/{page}")
    public String getAllForYear(@PathVariable int year, Model model,@PathVariable int page){

        boolean hasExistingDevice;



        List<IssueDocumentDTO> docs= issueDocService.findByYear(year,page-1,10);

        List<String> numbers = docs.stream().map(d->d.getInventNumber()).collect(Collectors.toList());


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


        model.addAttribute("numbers",numbers);
        model.addAttribute("amount",issueDocService.numberByYear(year));
        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("year",year);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("docList",docs);
        return "documents/getAllForYear";
    }


    public String addFormDamErr(@PathVariable Integer damageId, Model model, IssueDocumentDTO documentDTO){
        model.addAttribute("newDoc", documentDTO);
        String inventNumber = damageDAO.findById(damageId).orElse(null).getDevice().getInventNumber();
        model.addAttribute("inventNumber", inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);
        model.addAttribute("damageId",damageId);
        return "documents/addDocFormDam";
    }

    public String addFormDevErr(@PathVariable String inventNumber, Model model, IssueDocumentDTO issueDocumentDTO){
        model.addAttribute("newDoc",issueDocumentDTO);
        model.addAttribute("inventNumber", inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);

        return "documents/addDocFormDev";
    }

  @GetMapping("/modalDocs/")
    public String modalDocs(Model model, @ModelAttribute("docs") int year){

        return getAllForYear(year,model,1);
  }





}
