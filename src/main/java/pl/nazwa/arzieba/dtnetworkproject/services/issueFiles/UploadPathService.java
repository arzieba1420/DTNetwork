package pl.nazwa.arzieba.dtnetworkproject.services.issueFiles;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;

@Service
public interface UploadPathService {

    String issueDocs(Model model);
    File getFilePath(String modifiedFileName, String path);
}
