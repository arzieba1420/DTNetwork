package pl.nazwa.arzieba.dtnetworkproject.services.downloadItem;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.dao.DownloadItemDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.DownloadItem;
import pl.nazwa.arzieba.dtnetworkproject.utils.downloadItem.DownloadItemMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DownloadItemServiceImpl implements DownloadItemService {

    private final DownloadItemDAO dao;

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
}
