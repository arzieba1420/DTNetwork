package pl.nazwa.arzieba.dtnetworkproject.services.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueFilesDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.utils.media.MediaTypeUtils;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
public class DownloadServiceImpl implements DownloadService {

    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    private final IssueFilesDAO filesDAO;
    private final ServletContext SERVLET_CONTEXT;
    private static final String STORAGE_PATH = MainController.applicationHome.getDir().getAbsolutePath();
    public static final String STORAGE_DIRECTORY = STORAGE_PATH + "/storage";
    private static final String FILE_DIRECTORY = STORAGE_DIRECTORY + "/files";
    //HAS TO BE PROVIDED
    private static final String DEFAULT_FILE_NAME = "static/files/EmptyCard.pdf";

    //--------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------------

    @Autowired
    public DownloadServiceImpl(IssueFilesDAO filesDAO, ServletContext servlet_context) {
        this.filesDAO = filesDAO;
        SERVLET_CONTEXT = servlet_context;
    }

    //------------------------------------------------------------CONTROLLER METHODS---------------------------------------------------------------------------------------------

    @Override
    public void downloadDefault(String fileName, HttpServletResponse response) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();


            InputStream inputStream = classLoader.getResourceAsStream("static/files/" + fileName + ".pdf");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=" + fileName));
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
    }



    @Override
    public ResponseEntity<InputStreamResource> downloadIssueDocFile(Long fileID) throws FileNotFoundException {
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

    @Override
    public ResponseEntity<InputStreamResource> downloadDeviceCard(String fileName) throws FileNotFoundException {
        return null;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile1(String fileName, HttpServletResponse response) throws IOException {

        if (fileName.equals("Instrukcja")){
            downloadDefault("Instrukcja", response);
            return new ResponseEntity<InputStreamResource>(HttpStatus.OK);
        }

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.SERVLET_CONTEXT, fileName);
        File file;
        InputStreamResource resource;

        try {
            file = new File(STORAGE_DIRECTORY + "/" + fileName+".pdf");
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            /*file = new File(STORAGE_DIRECTORY + "/" + DEFAULT_FILE_NAME);
            resource = new InputStreamResource(new FileInputStream(file));*/
            downloadDefault("EmptyCard", response);
            return new ResponseEntity<InputStreamResource>(HttpStatus.OK);
                    // Content-Disposition

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

    private ResponseEntity<InputStreamResource> getDeviceCard(String fileName, InputStreamResource resource, File file){
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.SERVLET_CONTEXT, fileName);
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
