package pl.nazwa.arzieba.dtnetworkproject.services.issueFiles;

import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.controllers.DownloadController;
import pl.nazwa.arzieba.dtnetworkproject.services.download.DownloadServiceImpl;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;

@Service
@Transactional
public class UploadPathServiceImpl implements UploadPathService {

    private ServletContext context;
    private DownloadServiceImpl downloadServiceImpl;
    private String DIRECTORY = downloadServiceImpl.STORAGE_DIRECTORY;

    public UploadPathServiceImpl(ServletContext context, DownloadServiceImpl downloadServiceImpl) {
        this.context = context;
        this.downloadServiceImpl = downloadServiceImpl;
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
