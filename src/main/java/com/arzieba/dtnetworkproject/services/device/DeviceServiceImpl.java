package com.arzieba.dtnetworkproject.services.device;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.model.IssueDocument;
import com.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
       return deviceDAO.findAll().stream().map(d-> DeviceMapper.map(d)).collect(Collectors.toList());
    }

    //TODO validation IF EXIST
    @Override
    public DeviceDTO findByInventNumber(String inventNumber) {
        return DeviceMapper.map( deviceDAO.findByInventNumber(inventNumber));
    }

    @Override
    public List<DeviceDTO> findByType(String name) {

        System.out.println();

      return deviceDAO.findAll().stream()
              .filter(d->d.getDeviceType()
              .name().equals(name))
              .map(d->DeviceMapper.map(d))
              .collect(Collectors.toList());

    }

    @Override
    public DeviceDTO create(DeviceDTO toAdd) {
        return null;
    }

    @Override
    public DeviceDTO update(DeviceDTO deviceDTO) {
        return null;
    }

    @Override
    public DeviceDTO remove(String inventNumber) {
        return null;
    }

    @Override
    public List<DamageDTO> getDamages(String inventNumber) {
        return null;
    }

    @Override
    public List<IssueDocument> getIssueDocuments(String inventNumber) {
        return null;
    }

    @Override
    public DeviceCard getDeviceCard(String inventNumber) {
        return null;
    }




}
