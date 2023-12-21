package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.download.DownloadService;
import pl.nazwa.arzieba.dtnetworkproject.services.downloadItem.DownloadItemService;

import javax.validation.Valid;

@Controller
@RequestMapping("/files")
public class DownloadItemsController {

    private final DownloadItemService downloadItemService;


    public DownloadItemsController(DownloadItemService downloadItemService) {
        this.downloadItemService = downloadItemService;
    }

    @GetMapping
    public String getAllFiles(Model model){
        return downloadItemService.getAllFiles(model);
    }

    @GetMapping("/newFile")
    public String addForm(Model model) {
        return downloadItemService.addForm(model);
    }

    @PostMapping("/upload")
    public String uploadFile(Model model, @Valid @ModelAttribute("fileDTO")  DownloadItemDTO fileDTO, BindingResult bindingResult) {

       return  downloadItemService.save(model, fileDTO, bindingResult);
    }
}
