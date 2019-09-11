package pl.nazwa.arzieba.dtnetworkproject.services.issueFiles;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface UploadPathService {
    File getFilePath(String modifiedFileName, String path);
}
