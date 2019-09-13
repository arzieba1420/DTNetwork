package pl.nazwa.arzieba.dtnetworkproject.services.issueFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;

@Service
@Transactional
public class UploadPathServiceImpl implements UploadPathService {

    public static final String DIRECTORY = "C://Users/pc/Desktop/DTNetwork/storage/";

    @Autowired
    ServletContext context;

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
