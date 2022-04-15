package pl.nazwa.arzieba.dtnetworkproject.services.issueFiles;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.services.download.DownloadServiceImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadPathServiceImpl implements UploadPathService {

    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    private DownloadServiceImpl downloadServiceImpl;
    private String DIRECTORY = downloadServiceImpl.STORAGE_DIRECTORY;
    private IssueDocumentDAO issueDocumentDAO;

    //---------------------------------------------------------------CONSTRUCTOR---------------------------------------------------------------------------------------------

    public UploadPathServiceImpl(DownloadServiceImpl downloadServiceImpl, IssueDocumentDAO issueDocumentDAO) {
        this.downloadServiceImpl = downloadServiceImpl;
        this.issueDocumentDAO = issueDocumentDAO;
    }

    //------------------------------------------------------------CONTROLLER METHODS---------------------------------------------------------------------------------------------

    @Override
    public String issueDocs(Model model) {
        List<IssueDocument> issueDocuments = issueDocumentDAO.findAll();
        model.addAttribute("documents", issueDocuments);
        model.addAttribute("document", new IssueDocument());
        model.addAttribute("issueFiles", new ArrayList<IssueFiles>());
        model.addAttribute("isAdd",true);
        return "view/issue";
    }

    @Override
    public File getFilePath(String modifiedFileName, String path) {
        boolean exists =new File(DIRECTORY+"/"+path+"/").exists();
        String modifiedFilePath = DIRECTORY+"/"+path+"/"+File.separator+modifiedFileName;
        File file = new File(modifiedFilePath);

        if(!exists){
            new File(DIRECTORY+"/"+path+"/").mkdir();
        }
        return file;
    }
}
