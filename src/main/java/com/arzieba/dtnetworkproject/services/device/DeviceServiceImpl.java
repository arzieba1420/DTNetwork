package com.arzieba.dtnetworkproject.services.device;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.model.IssueDocument;
import com.arzieba.dtnetworkproject.model.Room;
import com.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import com.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import com.arzieba.dtnetworkproject.utils.deviceCard.DeviceCardMapper;
import com.arzieba.dtnetworkproject.utils.exceptions.DeviceNotFoundException;
import com.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    public DeviceServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
    }

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;


    @Override
    public List<DeviceDTO> findAll() {
       return deviceDAO.findAll()
               .stream()
               .map(DeviceMapper::map)
               .collect(Collectors.toList());
    }


    @Override
    public DeviceDTO findByInventNumber(String inventNumber) {
        if(!this.findAll().stream()
                .map(DeviceDTO::getInventNumber)
                .collect(Collectors.toList())
                .contains(inventNumber)) throw new DeviceNotFoundException();
        return DeviceMapper.map( deviceDAO.findByInventNumber(inventNumber));
    }

    @Override
    public List<DeviceDTO> findByType(String name) {

        System.out.println();

      return deviceDAO.findAll().stream()
              .filter(d->d.getDeviceType()
              .name().equals(name))
              .map(DeviceMapper::map)
              .collect(Collectors.toList());

    }

    @Override
    public DeviceDTO create(DeviceDTO toAdd) {
        deviceDAO.save( DeviceMapper.map(toAdd));
        return toAdd;
    }

    @Override
    public DeviceDTO update(DeviceDTO deviceDTO) {
        if(!this.findAll().stream()
                .map(DeviceDTO::getInventNumber)
                .collect(Collectors.toList())
                .contains(deviceDTO.getInventNumber())){
            return  create(deviceDTO);

        } else{
           Device toBeUpdated = deviceDAO.findByInventNumber(deviceDTO.getInventNumber());
           toBeUpdated.setDeviceType(deviceDTO.getDeviceType());
           toBeUpdated.setDeviceDescription(deviceDTO.getDeviceDescription());
           toBeUpdated.setRoom(deviceDTO.getRoom());
           deviceDAO.save(toBeUpdated);
           return DeviceMapper.map(toBeUpdated);
        }
    }

    @Override
    public DeviceDTO remove(String inventNumber) {
        if(!this.findAll().stream()
                .map(DeviceDTO::getInventNumber)
                .collect(Collectors.toList())
                .contains(inventNumber)) throw new DeviceNotFoundException("Device not found");
        else {
            DeviceDTO removed = findByInventNumber(inventNumber);
            deviceDAO.deleteDeviceByInventNumber(inventNumber);
            removed.setDeviceDescription("Removed " + removed.getDeviceDescription());
            return removed;
        }

    }

    @Override
    public List<DamageDTO> getDamages(String inventNumber) {
        return deviceDAO.findByInventNumber(inventNumber)
                .getDamageList()
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDocumentDTO> getIssueDocuments(String inventNumber) {

        return issueDocumentDAO.findByInventNumber(inventNumber).stream()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());

    }

    @Override
    public DeviceCardDTO getDeviceCard(String inventNumber) {
        return DeviceCardMapper.map(deviceCardDAO.findByDevice_InventNumber(inventNumber));
    }

    @Override
    public List<DeviceDTO> findByRoom(String room) {
        return deviceDAO.findAll().stream().filter(d->d.getRoom().equals(Room.valueOf(room)))
                .map(DeviceMapper::map)
                .collect(Collectors.toList());
    }


}
