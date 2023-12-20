package pl.nazwa.arzieba.dtnetworkproject.services.upload;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public interface FileUploadService {
    public void init();

    public void save(MultipartFile file);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

    String addForm(Model model);
}
