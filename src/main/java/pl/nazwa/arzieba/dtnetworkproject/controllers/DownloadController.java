package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.nazwa.arzieba.dtnetworkproject.services.download.DownloadService;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class DownloadController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    public static final String STORAGE_PATH = MainController.applicationHome.getDir().getAbsolutePath();
    private final DownloadService downloadService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    //--------------------------------------------------------------------CONSTRUCTOR--------------------------------------------------------------------------------------------

    @Autowired
    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PostConstruct
    void paths(){
        logger.info(MainController.applicationHome.getDir().getAbsolutePath());
        logger.info(STORAGE_PATH);
    }

    //---------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    //Download file from STORAGE DIRECTORY when its name is given in URL
    //Pobieranie pliku karty maszyny dla urządzenia o numerze inwentarzowym jak w URL
    @RequestMapping("/download1/{fileName}")
    public ResponseEntity <InputStreamResource> downloadCardsAndInstruction(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        return downloadService.downloadFile1(fileName, response);
    }

    //Download file from FILE DIRECTORY when its name is given in URL
    //Pobieranie pliku ZAMÓWIENIA o ID jak w URL
    //Unactive in DEMO Version
    @GetMapping("/downloadDocFile/{fileID}")
    public ResponseEntity<InputStreamResource> downloadIssueDocFile(@PathVariable Long fileID) throws IOException {
        return downloadService.downloadIssueDocFile(fileID);
    }
}
