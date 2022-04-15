package pl.nazwa.arzieba.dtnetworkproject.controllers;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/issueDocs")
public class IssueDocController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    private IssueDocService issueDocService;

    //---------------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------

    @Autowired
    public IssueDocController(IssueDocService issueDocService) {
        this.issueDocService = issueDocService;
    }

    //--------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    //Shows specified page of all docs for given Device
    //Zwraca konkretną stronę z dokumentami dla danego urządzenia
    @GetMapping("/devices/{inventNumber}/{page}")
    public String showDocsforDevice(@PathVariable String inventNumber, Model model, @PathVariable int page){
        return issueDocService.showDocsforDevice(inventNumber, model, page);
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addFormDevice(@PathVariable String inventNumber, Model model){
        return issueDocService.addFormDevice(inventNumber, model);
    }

    @GetMapping("/addFormDam/{damageId}")
    public String addFormDamage(@PathVariable Integer damageId, Model model){
        return issueDocService.addFormDamage(damageId, model);
    }

    @PostMapping("/addAsModel/stay")
    public String createDocumentForDamage(@Valid @ModelAttribute("newDoc")  IssueDocumentDTO issueDocumentDTO,
                                          BindingResult bindingResult, Model model, HttpServletRequest request){
        return issueDocService.createDocumentForDamage(issueDocumentDTO, bindingResult, model, request);
    }

    @PostMapping("/addAsModel/stay2")
    public String createDocumentForDevice(@Valid @ModelAttribute("newDoc")  IssueDocumentDTO issueDocumentDTO,
                                          BindingResult bindingResult, Model model, HttpServletRequest request,
                                          RedirectAttributes redirectAttributes){
        return issueDocService.createDocumentForDevice(issueDocumentDTO, bindingResult, model, request, redirectAttributes);
    }

    @GetMapping("/damages/{damageId}")
    public String showDocsForDamage(@PathVariable Integer damageId, Model model){
        return issueDocService.showDocsForDamage(damageId, model);
    }

    @GetMapping("/{year}/{page}")
    public String showDocsForYear(@PathVariable int year, Model model, @PathVariable int page){
        return issueDocService.showDocsForYear(year, model, page);
    }

  @GetMapping("/modalDocs/")
    public String modalDocs(Model model, @ModelAttribute("docs") int year){
        return showDocsForYear(year,model,1);
  }

  @GetMapping("/showFiles")
    public String showFilesForDoc(Model model,@ModelAttribute("doc1") String signature){
        return issueDocService.showFilesForDoc(model, signature);
  }

  @GetMapping("/editDoc")
    public String createEditForm(Model model, @ModelAttribute("eDoc") String signature ){
      return issueDocService.createEditForm(model, signature);
  }

  @PostMapping("/editDocPost")
    public String editDoc(@Valid @ModelAttribute ("editedDoc") IssueDocumentDTO documentDTO,BindingResult bindingResult, Model model){
        return issueDocService.editDoc(documentDTO, bindingResult, model);
  }
}
