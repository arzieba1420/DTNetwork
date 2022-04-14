package pl.nazwa.arzieba.dtnetworkproject.services.device;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import pl.nazwa.arzieba.dtnetworkproject.dto.*;


import javax.validation.Valid;
import java.util.List;

public interface DeviceService {

     List<DeviceDTO> findAll();
     DeviceDTO generateMainViewForDevice(String inventNumber);
     List<DeviceDTO> findByType(String name);
     DeviceDTO create(DeviceDTO toAdd);
     DeviceDTO update(DeviceDTO deviceDTO);
     DeviceDTO remove(String inventNumber);
     List<DamageDTO> getDamages (String inventNumber);
     List<IssueDocumentDTO> getIssueDocuments (String inventNumber);
     DeviceCardDTO getDeviceCard(String inventNumber);
     List<DeviceDTO> findByRoom(String room);
     DeviceDTO changeRoom(String inventNumber);
     String generateMainViewForDevice(String inventNumber, Model model);
     String createDevice(DeviceDTO dto, BindingResult bindingResult, Model model);
     String createDeviceInRoom(Model model, String room);
     String createActivityForGenerator(Model model, String inventNumber);

     String setChillerTemp(ChillerSetDTO chillerSetDTO, BindingResult binding, Model model,String inventNumber);
     String setDrycoolerTemp( DrycoolerSetDTO drycoolerSetDTO, BindingResult binding, Model model,String inventNumber);
     String getAllGeneratorTests(String inv, Model model, int page);
}
