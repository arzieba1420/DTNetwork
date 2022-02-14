package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.nazwa.arzieba.dtnetworkproject.configuration.MyPropertiesConfig;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueFilesDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.utils.media.MediaTypeUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class DownloadController {


    @Autowired
    private ServletContext servletContext;
    @Autowired
    IssueFilesDAO filesDAO;



    private static final String STORAGE_PATH = System.getProperty("user.home");
    public static final String STORAGE_DIRECTORY = STORAGE_PATH + "/Desktop/DTNetwork/storage";
    private static final String FILE_DIRECTORY = STORAGE_PATH + "/Desktop/DTNetwork/storage/files";
    private static final String DEFAULT_FILE_NAME = "EmptyCard.pdf";

    // http://localhost:8080/download1?fileName=abc.zip
    // Using ResponseEntity<InputStreamResource>
    @RequestMapping("/download1/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile1(
            @PathVariable String fileName) throws IOException {

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);


        File file = null;
        InputStreamResource resource = null;
        try {
            file = new File(STORAGE_DIRECTORY + "/" + fileName+".pdf");

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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }

    @GetMapping("/downloadDocFile/{fileID}")
    public ResponseEntity<InputStreamResource> downloadFile2(
            @PathVariable Long fileID) throws IOException {

        IssueFiles issueFile = filesDAO.findByIssueFilesId(fileID);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, issueFile.getFileNameNoExt());


        File file = null;
        InputStreamResource resource = null;
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
