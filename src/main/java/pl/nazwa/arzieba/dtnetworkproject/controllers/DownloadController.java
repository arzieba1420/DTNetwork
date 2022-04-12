package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueFilesDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.utils.media.MediaTypeUtils;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class DownloadController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //-------------------------------VARIABLES--------------------------------------------------
    private final ServletContext SERVLET_CONTEXT;
    private IssueFilesDAO filesDAO;
    private static final String STORAGE_PATH = MainController.applicationHome.getDir().getAbsolutePath();
    public static final String STORAGE_DIRECTORY = STORAGE_PATH + "/storage";
    private static final String FILE_DIRECTORY = STORAGE_DIRECTORY + "/files";
    //HAS TO BE PROVIDED
    private static final String DEFAULT_FILE_NAME = "EmptyCard.pdf";
    private ResourceLoader resourceLoader;

    //-------------------------------CONSTRUCTOR--------------------------------------------------
    public DownloadController(ServletContext SERVLET_CONTEXT, IssueFilesDAO filesDAO, ResourceLoader resourceLoader) {
        this.SERVLET_CONTEXT = SERVLET_CONTEXT;
        this.filesDAO = filesDAO;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    void paths(){
        logger.info(MainController.applicationHome.getDir().getAbsolutePath());
        logger.info(STORAGE_PATH);

    }

    //-------------------------------BUSINESS LOGIC--------------------------------------------------

    //Download file from STORAGE DIRECTORY when its name is given in URL
    //Pobieranie pliku karty maszyny dla urządzenia o numerze inwentarzowym jak w URL
    @RequestMapping("/download1/{fileName}")
    public void downloadFile1(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();


            if (fileName.equalsIgnoreCase("Instrukcja")){
            InputStream inputStream = classLoader.getResourceAsStream(fileName+".pdf");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename="+fileName));
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, response.getOutputStream());}

            if(!fileName.equalsIgnoreCase("Instrukcja")) {
                InputStream inputStream = classLoader.getResourceAsStream("EmptyCard.pdf");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition",
                        String.format("attachment; filename=EmptyCard"));
                response.setContentLength(inputStream.available());
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }
    }

    //Download file from FILE DIRECTORY when its name is given in URL
    //Pobieranie pliku ZAMÓWIENIA o ID jak w URL
    @GetMapping("/downloadDocFile/{fileID}")
    public ResponseEntity<InputStreamResource> downloadFile2(@PathVariable Long fileID) throws IOException {

        IssueFiles issueFile = filesDAO.findByIssueFilesId(fileID);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.SERVLET_CONTEXT, issueFile.getFileNameNoExt());
        File file;
        InputStreamResource resource;

        try {
            file = new File(FILE_DIRECTORY + "/" + issueFile.getFileNameNoExt()+"."+issueFile.getFileExtension());
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            file = new File(STORAGE_DIRECTORY + "/" + DEFAULT_FILE_NAME);
            resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    // Content-Type
                    .contentType(mediaType)
                    // Contet-Length
                    .contentLength(file.length()) //
                    .body(resource);
        }
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + issueFile.getFileName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
