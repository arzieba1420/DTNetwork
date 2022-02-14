package pl.nazwa.arzieba.dtnetworkproject.services.issueFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.controllers.DownloadController;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;

@Service
@Transactional
public class UploadPathServiceImpl implements UploadPathService {




    final
    ServletContext context;

    private DownloadController downloadController;

    public UploadPathServiceImpl(ServletContext context, DownloadController downloadController) {
        this.context = context;
        this.downloadController = downloadController;
    }

    private String DIRECTORY = downloadController.STORAGE_DIRECTORY;


    @Override
    public File getFilePath(String modifiedFileName, String path) {


        boolean exists =new File(DIRECTORY+"/"+path+"/").exists();
        if(!exists){
            new File(DIRECTORY+"/"+path+"/").mkdir();
        }

        String modifiedFilePath = DIRECTORY+"/"+path+"/"+File.separator+modifiedFileName;
        File file = new File(modifiedFilePath);
        return file;
    }
}
