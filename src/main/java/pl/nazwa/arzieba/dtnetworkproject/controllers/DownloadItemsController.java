package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.nazwa.arzieba.dtnetworkproject.services.download.DownloadService;
import pl.nazwa.arzieba.dtnetworkproject.services.downloadItem.DownloadItemService;

@Controller
public class DownloadItemsController {

    private final DownloadItemService downloadItemService;


    public DownloadItemsController(DownloadItemService downloadItemService) {
        this.downloadItemService = downloadItemService;
    }

    @GetMapping("/files")
    public String getAllFiles(Model model){
        return downloadItemService.getAllFiles(model);
    }

}
