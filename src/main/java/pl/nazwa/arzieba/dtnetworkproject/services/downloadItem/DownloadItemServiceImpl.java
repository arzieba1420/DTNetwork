package pl.nazwa.arzieba.dtnetworkproject.services.downloadItem;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.dao.DownloadItemDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.DownloadItem;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.services.load.LoadService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.downloadItem.DownloadItemMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DownloadItemServiceImpl implements DownloadItemService {

    private final DownloadItemDAO dao;
    private final String root = MainController.applicationHome.getDir().getAbsolutePath() + "/storage";

    private final ShortPostService postService;

    private final MainController mainController;

    public DownloadItemServiceImpl(DownloadItemDAO dao, ShortPostService postService, MainController mainController) {
        this.dao = dao;
        this.postService = postService;
        this.mainController = mainController;
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
        List<String> fields = new ArrayList<>();
        model.addAttribute("itemDTO", itemDTO);
        model.addAttribute("fields", fields);
        return "files/addFileForm";
    }

    public String addFormErr(Model model, DownloadItemDTO itemDTO){
        model.addAttribute("itemDTO", itemDTO);
        return "files/addFileForm";
    }

    @Override
    public String save(Model model, DownloadItemDTO itemDTO, BindingResult bindingResult) {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            List<String> fields = allErrors.stream().map(FieldError::getField).collect(Collectors.toList());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("fields", fields);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());

            return addFormErr(model, itemDTO);
        }


        try {
            MultipartFile file = itemDTO.getFile();
            Files.createDirectories(Paths.get(root));
            file.transferTo(Paths.get(root+"/"+file.getOriginalFilename()));
            DownloadItemDTO toSave = DownloadItemDTO.builder()
                    .type(file.getContentType())
                    .description(itemDTO.getDescription())
                    .name(file.getOriginalFilename())
                    .build();
            dao.save(DownloadItemMapper.map(toSave));

        } catch (Exception e) {
            FieldError fieldError = new FieldError("itemDTO", "file", "SELECTED FILE" ,
                    false, null, null, "Wystąpił błąd: "+ e.getMessage());
            bindingResult.addError(fieldError);
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return addFormErr(model, itemDTO);
        }


        postService.create(new ShortPostDTO(
                Author.DTN,
                "Został dodany nowy plik w sekcji \"Do pobrania\" [SYSTEM]",
                CalendarUtil.cal2string(Calendar.getInstance()),
                "DTN",
                false,
                PostLevel.INFO
        ));
        return "redirect:/dtnetwork";
    }
}

