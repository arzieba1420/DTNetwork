package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.nazwa.arzieba.dtnetworkproject.services.issueFiles.UploadPathService;

@Controller
public class IssueFileUploadController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    private UploadPathService uploadPathService;

    //---------------------------------------------------------------------CONSTRUCTOR----------------------------------------------------------------------------------------

    @Autowired
    public IssueFileUploadController(UploadPathService uploadPathService) {
        this.uploadPathService = uploadPathService;
    }

    //--------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    @GetMapping("/issueFileUpload")
    public String issueDocs(Model model){
        return uploadPathService.issueDocs(model);
    }
}
