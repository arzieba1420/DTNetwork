package pl.nazwa.arzieba.dtnetworkproject.services.deviceCard;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import java.util.List;

@Service
public interface DeviceCardService {

    List<DeviceCardDTO> findAll();
    List<String> showAllSignatures();
    DeviceCardDTO findForDevice(String inv);
    DeviceCardDTO findForSignature(String signature);
    DeviceCardDTO findForId(Integer id);
    String create(DeviceCardDTO dto);
    String addForm(Model model,String inventNumber);
    String create(Model model,DeviceCardDTO dto, BindingResult bindingResult);
}
