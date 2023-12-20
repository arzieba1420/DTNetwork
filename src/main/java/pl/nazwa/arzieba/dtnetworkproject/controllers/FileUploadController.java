package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.services.upload.FileUploadService;

@Controller
public class FileUploadController {

    private FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }


 /*   @PostMapping("/files/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) {
        String message = "";

        try {
            fileUploadService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "files/allFiles";
    }

*/


}

