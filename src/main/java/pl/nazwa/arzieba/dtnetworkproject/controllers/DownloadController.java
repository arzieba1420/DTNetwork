package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.nazwa.arzieba.dtnetworkproject.utils.media.MediaTypeUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class DownloadController {
    private static final String DIRECTORY = "C:/Users/User/Desktop/DTNetwork/storage";
    private static final String DEFAULT_FILE_NAME = "S-123.pdf";

    @Autowired
    private ServletContext servletContext;

    // http://localhost:8080/download1?fileName=abc.zip
    // Using ResponseEntity<InputStreamResource>
    @RequestMapping("/download1/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile1(
            @PathVariable String fileName) throws IOException {

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);

        File file = new File(DIRECTORY + "/" + fileName+".pdf");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
