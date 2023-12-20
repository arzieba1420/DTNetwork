package pl.nazwa.arzieba.dtnetworkproject.services.upload;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Path root = Paths.get(MainController.applicationHome.getDir().getAbsolutePath() + "/storage");
    @Override
    public void init() {

    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public String addForm(Model model) {
        model.addAttribute("fileDTO");
        return "files/addFileForm";
    }
}
