package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IssueFileUploadController {

    @Autowired
    private IssueDocService issueDocService;
    private IssueDocumentDAO issueDocumentDAO;

    @GetMapping("/issueFileUpload")
    public String issueDocs(Model model){
        List<IssueDocument> issueDocuments = issueDocumentDAO.findAll();
        model.addAttribute("documents", issueDocuments);
        model.addAttribute("document", new IssueDocument());
        model.addAttribute("issueFiles", new ArrayList<IssueFiles>());
        model.addAttribute("isAdd",true);
        return "view/issue";
    }

}
