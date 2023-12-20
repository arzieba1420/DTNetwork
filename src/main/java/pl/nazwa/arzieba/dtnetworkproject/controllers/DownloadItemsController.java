package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;
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

    @GetMapping("/files/newFile")
    public String addForm(Model model) {
        return downloadItemService.addForm(model);
    }

    @PostMapping("/files/upload")
    public String uploadFile(Model model, @ModelAttribute("fileDTO") DownloadItemDTO fileDTO) {

       return  downloadItemService.save(model, fileDTO);
    }
}
