package pl.nazwa.arzieba.dtnetworkproject.services.deviceCard;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;

import javax.validation.Valid;
import java.util.List;

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
