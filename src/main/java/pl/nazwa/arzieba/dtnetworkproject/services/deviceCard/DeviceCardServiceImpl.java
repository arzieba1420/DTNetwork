package pl.nazwa.arzieba.dtnetworkproject.services.deviceCard;

import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.DeviceCard;
import pl.nazwa.arzieba.dtnetworkproject.utils.deviceCard.DeviceCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceCardServiceImpl implements DeviceCardService {

    private DeviceCardDAO dao;
    private DeviceDAO deviceDAO;

    @Autowired
    public DeviceCardServiceImpl(DeviceCardDAO dao, DeviceDAO deviceDAO) {
        this.dao = dao;
        this.deviceDAO = deviceDAO;
    }

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
}
