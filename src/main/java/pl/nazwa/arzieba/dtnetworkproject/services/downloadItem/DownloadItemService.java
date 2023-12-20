package pl.nazwa.arzieba.dtnetworkproject.services.downloadItem;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.dto.DownloadItemDTO;

import java.util.List;

@Service
public interface DownloadItemService {

    String getAllFiles(Model model);

    String addForm(Model model);

    String save(Model model, DownloadItemDTO itemDTO);
}
