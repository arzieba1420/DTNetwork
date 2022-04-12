package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/issueDocs")
public class IssueDocController {

//--------------------------------------VARIABLES------------------------------------------------
    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private IssueDocService issueDocService;
    private DeviceService deviceService;
    private ShortPostService postService;
    @Value("${my.pagesize}")
    int pagesize;

    //--------------------------------------CONSTRUCTOR------------------------------------------------
    @Autowired
    public
    IssueDocController(DamageService damageService, DeviceService deviceService, DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, IssueDocService issueDocService, ShortPostDAO postDAO, ShortPostService postService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.issueDocService = issueDocService;
        this.deviceService=deviceService;
        this.damageService=damageService;
        this.postService = postService;
    }

    //--------------------------------------BUSINESS LOGIC------------------------------------------------

    //Shows specified page of all docs for given Device
    //Zwraca konkretną stronę z dokumentami dla danego urządzenia
    @GetMapping("/devices/{inventNumber}/{page}")
    public String findByInventNumber(@PathVariable String inventNumber, Model model, @PathVariable int page){

        List<IssueDocumentDTO> docs = deviceService.getIssueDocuments(inventNumber);
        DeviceDTO dto = deviceService.findByInventNumber(inventNumber);
        List<IssueDocumentDTO> page1= issueDocService.findByDevice(inventNumber,page-1,pagesize);
        List<String> pageNumbers = docs.stream().map(d->d.getInventNumber()).collect(Collectors.toList());
        int numberOfPages = (issueDocService.numberByDevice(inventNumber))/pagesize + 1;
        int i = 2;
        int lastPage = 1;

        //prevents empty last page
        //zapobiega tworzeniu ostatniej pustej strony
        if(issueDocService.numberByDevice(inventNumber)%pagesize==0){
            numberOfPages--;
        }
        List<Integer> morePages = new LinkedList<>();

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("numbers",pageNumbers);
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

        IssueDocumentDTO dto = new IssueDocumentDTO();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        dto.setIssueDate(CalendarUtil.cal2string(Calendar.getInstance()));
        model.addAttribute("newDoc", dto);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);

        return "documents/addDocFormDev";
    }

    @GetMapping("/addFormDam/{damageId}")
    public String addFormDam(@PathVariable Integer damageId, Model model){

        IssueDocumentDTO dto = new IssueDocumentDTO();
        String inventNumber = damageDAO.findById(damageId).orElse(null).getDevice().getInventNumber();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        dto.setIssueDate(CalendarUtil.cal2string(Calendar.getInstance()));
        model.addAttribute("newDoc", dto);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);
        model.addAttribute("damageId",damageId);

        return "documents/addDocFormDam";
    }


    @PostMapping("/addAsModel/stay")
    public String  create3(@Valid @ModelAttribute("newDoc")  IssueDocumentDTO issueDocumentDTO, BindingResult bindingResult,Model model, HttpServletRequest request){

        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
                FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }

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
        ShortPostDTO shortPostDTO = new ShortPostDTO();
        shortPostDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        shortPostDTO.setForDamage(false);
        shortPostDTO.setContent("Wprowadzono nowe zamówienie dla usterki! [SYSTEM]");
        shortPostDTO.setPostLevel(PostLevel.INFO);
        shortPostDTO.setAuthor(Author.DTN);
        shortPostDTO.setInventNumber(issueDocumentDTO.getInventNumber());
        postService.create(shortPostDTO);

        return "redirect:/dtnetwork";
    }

    @PostMapping("/addAsModel/stay2")
    public String  create(@Valid @ModelAttribute("newDoc")  IssueDocumentDTO dto, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){

        ShortPostDTO postDTO = new ShortPostDTO();

        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            if(issueDocumentDAO.existsByIssueSignature(dto.getIssueSignature())){
                FieldError fieldError = new FieldError("newDoc","issueSignature", dto.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }

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
        postDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        postDTO.setForDamage(false);
        postDTO.setContent("Wprowadzono nowe zamówienie dla urządzenia! [SYSTEM]");
        postDTO.setPostLevel(PostLevel.INFO);
        postDTO.setAuthor(Author.DTN);
        postDTO.setInventNumber(dto.getInventNumber());
        postService.create(postDTO);

        return "redirect:/dtnetwork";
    }

    @GetMapping("/damages/{damageId}")
    public String getAllForDamage(@PathVariable Integer damageId, Model model){

        String inventNumber = damageService.findById(damageId).getDeviceInventNumber();
        DeviceDTO deviceDTO= deviceService.findByInventNumber(inventNumber) ;
        List<IssueDocumentDTO> docs= issueDocService.findByDamageId(damageId);

        model.addAttribute("amount", docs.size());
        model.addAttribute("docs",docs);
        model.addAttribute("dto",deviceDTO);
        model.addAttribute("damageId", damageId);

        return "damages/getAllDocs";
    }

    @GetMapping("/{year}/{page}")
    public String getAllForYear(@PathVariable int year, Model model,@PathVariable int page){

        boolean hasExistingDevice;
        List<IssueDocumentDTO> docs= issueDocService.findByYear(year,page-1,10);
        List<String> inventNumbers = docs.stream().map(d->d.getInventNumber()).collect(Collectors.toList());
        int numberOfPages = (issueDocService.numberByYear(year))/10 + 1;
        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

        if(issueDocService.numberByYear(year)%10==0){
            numberOfPages--;
        }

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("numbers",inventNumbers);
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


        String inventNumber = damageDAO.findById(damageId).orElse(null).getDevice().getInventNumber();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        model.addAttribute("newDoc", documentDTO);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);
        model.addAttribute("damageId",damageId);

        return "documents/addDocFormDam";
    }

    public String addFormDevErr(@PathVariable String inventNumber, Model model, IssueDocumentDTO issueDocumentDTO){

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        model.addAttribute("newDoc",issueDocumentDTO);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);

        return "documents/addDocFormDev";
    }

  @GetMapping("/modalDocs/")
    public String modalDocs(Model model, @ModelAttribute("docs") int year){

        return getAllForYear(year,model,1);
  }

  @GetMapping("/showFiles")
    public String allFilesForDoc(Model model,@ModelAttribute("doc1") String signature){

        List<IssueFiles> files = issueDocService.getFilesForDoc(signature);

        model.addAttribute("files", files);
        model.addAttribute("size", files.size());

        return "documents/allFilesForDoc";
  }

  @GetMapping("/editDoc")
    public String editDocument(Model model,@ModelAttribute("eDoc") String signature ){

      IssueDocumentDTO documentDTO = issueDocService.findBySignature(signature);
      String text = deviceDAO.findByInventNumber(documentDTO.getInventNumber()).getDeviceDescription()
              +" "+deviceDAO.findByInventNumber(documentDTO.getInventNumber()).getRoom();

      model.addAttribute("editedDoc", documentDTO);
      model.addAttribute("inventNumber", documentDTO.getInventNumber());
      model.addAttribute("text",text);

      return "documents/editForm";
  }

    public String editFormErr(Model model, String signature, IssueDocumentDTO documentDTO ){

        String text = deviceDAO.findByInventNumber(documentDTO.getInventNumber()).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(documentDTO.getInventNumber()).getRoom();

        model.addAttribute("editedDoc", documentDTO);
        model.addAttribute("inventNumber", documentDTO.getInventNumber());
        model.addAttribute("text",text);

        return "documents/editForm";
    }

  @PostMapping("/editDocPost")
    public String editDoc(@Valid @ModelAttribute ("editedDoc") IssueDocumentDTO documentDTO,BindingResult bindingResult, Model model){

        if (bindingResult.hasFieldErrors()) {
          List<FieldError> allErrors;
          allErrors = bindingResult.getFieldErrors();

          model.addAttribute("bindingResult", bindingResult);
          model.addAttribute("errors", allErrors);
          model.addAttribute("errorsAmount", allErrors.size());

          return editFormErr(model, documentDTO.getIssueSignature(),documentDTO);
      }

       issueDocService.create(documentDTO);

        return "redirect:/dtnetwork";
  }
}
