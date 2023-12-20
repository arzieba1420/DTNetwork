package pl.nazwa.arzieba.dtnetworkproject.services.downloadItem;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.dao.DownloadItemDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.DownloadItem;
import pl.nazwa.arzieba.dtnetworkproject.utils.downloadItem.DownloadItemMapper;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DownloadItemServiceImpl implements DownloadItemService {

    private final DownloadItemDAO dao;
    private final String root = MainController.applicationHome.getDir().getAbsolutePath() + "/storage";

    public DownloadItemServiceImpl(DownloadItemDAO dao) {
        this.dao = dao;
    }

    @Override
    public String getAllFiles(Model model) {
        Sort sort = Sort.by("id");
        List<DownloadItem> list = dao.findAll(sort);
        list.stream()
                .map( DownloadItemMapper::map )
                .collect(Collectors.toList());
        model.addAttribute("files", list);

        return "files/allFiles";
    }

    @Override
    public String addForm(Model model) {
        DownloadItemDTO itemDTO = new DownloadItemDTO();
        model.addAttribute("itemDTO", itemDTO);
        return "files/addFileForm";
    }

    @Override
    public String save(Model model, DownloadItemDTO itemDTO) {

        MultipartFile file = itemDTO.getFile();
        DownloadItemDTO toSave = DownloadItemDTO.builder()
                .type(file.getContentType())
                .description(itemDTO.getDescription())
                .name(file.getOriginalFilename())
                .build();
        dao.save(DownloadItemMapper.map(toSave));

        try {
            Files.createDirectories(Paths.get(root));
            file.transferTo(Paths.get(root+"/"+file.getOriginalFilename()));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return "redirect:/files/";
    }
}

