package pl.nazwa.arzieba.dtnetworkproject.services.deviceCard;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.DeviceCard;
import pl.nazwa.arzieba.dtnetworkproject.utils.deviceCard.DeviceCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceCardServiceImpl implements DeviceCardService {


    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    private DeviceCardDAO dao;
    private DeviceDAO deviceDAO;

    //--------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------------

    @Autowired
    public DeviceCardServiceImpl(DeviceCardDAO dao, DeviceDAO deviceDAO) {
        this.dao = dao;
        this.deviceDAO = deviceDAO;
    }

    //--------------------------------------------------------------DAO METHODS-----------------------------------------------------------------------------------------------

    @Override
    public List<DeviceCardDTO> findAll() {
        return dao.findAll().stream()
                .map(DeviceCardMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> showAllSignatures() {
        return findAll().stream()
                .map(DeviceCardDTO::getSignatureNumber)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceCardDTO findForDevice(String inv) {
        return DeviceCardMapper.map(dao.findByDevice_InventNumber(inv));
    }

    @Override
    public DeviceCardDTO findForSignature(String signature) {
        return DeviceCardMapper.map(dao.findBySignatureNumber(signature));
    }

    @Override
    public DeviceCardDTO findForId(Integer id) {
        return DeviceCardMapper.map(dao.findByDeviceCardID(id));
    }

    @Override
    public String create(DeviceCardDTO dto) {
       DeviceCard saved = dao.save(DeviceCardMapper.map(dto,deviceDAO));
        return saved.getSignatureNumber();
    }
    //------------------------------------------------------------------CONTROLLER METHODS---------------------------------------------------------------------------------

    @Override
    public String addForm(Model model, String inventNumber) {
        model.addAttribute("newCard", new DeviceCardDTO());
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("room", deviceDAO.findByInventNumber(inventNumber).getRoom());
        model.addAttribute("type", deviceDAO.findByInventNumber(inventNumber).getDeviceType());
        return "devices/addCardForm";
    }

    @Override
    public String create(Model model, DeviceCardDTO deviceCardDTO, BindingResult bindingResult) {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return addFormErr(model,deviceCardDTO.getInventNumber(),deviceCardDTO);
        }

        create(deviceCardDTO);
        return "redirect:/devices/"+deviceCardDTO.getInventNumber();
    }

    private String addFormErr(Model model, String inventNumber, DeviceCardDTO deviceCardDTO) {
        model.addAttribute("newCard", deviceCardDTO);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("room", deviceDAO.findByInventNumber(inventNumber).getRoom());
        model.addAttribute("type", deviceDAO.findByInventNumber(inventNumber).getDeviceType());
        return "devices/addCardForm";
    }
}
