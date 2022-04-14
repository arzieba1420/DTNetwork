package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/issueDocs")
public class IssueDocController {

//--------------------------------------LOCAL VARIABLES-----------------------------------------------------------------
    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private IssueDocService issueDocService;
    private DeviceService deviceService;
    private ShortPostService postService;


    //--------------------------------------CONSTRUCTOR-----------------------------------------------------------------
    @Autowired
    public
    IssueDocController(DamageService damageService, DeviceService deviceService, DeviceDAO deviceDAO, DamageDAO damageDAO,
                       IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, IssueDocService issueDocService,
                       ShortPostDAO postDAO, ShortPostService postService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.issueDocService = issueDocService;
        this.deviceService=deviceService;
        this.damageService=damageService;
        this.postService = postService;
    }

    //--------------------------------------BUSINESS LOGIC--------------------------------------------------------------

    //Shows specified page of all docs for given Device
    //Zwraca konkretną stronę z dokumentami dla danego urządzenia
    @GetMapping("/devices/{inventNumber}/{page}")
    public String showDocsforDevice(@PathVariable String inventNumber, Model model, @PathVariable int page){
        return issueDocService.showDocsforDevice(inventNumber, model, page);
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addFormDev(@PathVariable String inventNumber, Model model){
        return issueDocService.addFormDevice(inventNumber, model);
    }

    @GetMapping("/addFormDam/{damageId}")
    public String addFormDam(@PathVariable Integer damageId, Model model){
        return issueDocService.addFormDamage(damageId, model);
    }

    @PostMapping("/addAsModel/stay")
    public String createDocument(@Valid @ModelAttribute("newDoc")  IssueDocumentDTO issueDocumentDTO,
                                 BindingResult bindingResult, Model model, HttpServletRequest request){
        return issueDocService.createDocumentForDamage(issueDocumentDTO, bindingResult, model, request);
    }

    @PostMapping("/addAsModel/stay2")
    public String createDocumentAlternative(@Valid @ModelAttribute("newDoc")  IssueDocumentDTO issueDocumentDTO,
                                            BindingResult bindingResult, Model model, HttpServletRequest request,
                                            RedirectAttributes redirectAttributes){
        return issueDocService.createDocumentForDevice(issueDocumentDTO, bindingResult, model, request, redirectAttributes);
    }

    @GetMapping("/damages/{damageId}")
    public String getAllForDamage(@PathVariable Integer damageId, Model model){
        String inventNumber = damageService.findById(damageId).getDeviceInventNumber();
        DeviceDTO deviceDTO= deviceService.generateMainViewForDevice(inventNumber) ;
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
