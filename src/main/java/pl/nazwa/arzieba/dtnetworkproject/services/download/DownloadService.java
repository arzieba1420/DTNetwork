package pl.nazwa.arzieba.dtnetworkproject.services.download;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public interface DownloadService {
    void downloadDefault(String fileName, HttpServletResponse response) throws IOException;
    ResponseEntity<InputStreamResource> downloadIssueDocFile(Long fileID) throws FileNotFoundException;
    ResponseEntity<InputStreamResource> downloadDeviceCard (String fileName) throws FileNotFoundException;

    ResponseEntity<InputStreamResource> downloadFile1(String fileName, HttpServletResponse response) throws IOException;
}
