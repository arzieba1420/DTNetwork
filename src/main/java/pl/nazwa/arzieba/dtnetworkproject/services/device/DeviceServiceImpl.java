package pl.nazwa.arzieba.dtnetworkproject.services.device;

import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.*;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.deviceCard.DeviceCardMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DeviceNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest.GeneratorTestMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private GeneratorTestDAO generatorTestDAO;
    private ShortPostDAO shortPostDAO;

    @Autowired
    public DeviceServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, GeneratorTestDAO generatorTestDAO, ShortPostDAO shortPostDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.generatorTestDAO = generatorTestDAO;
        this.shortPostDAO = shortPostDAO;
    }

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

      return deviceDAO.findAll().stream()
              .filter(d->d.getDeviceType()
              .name().equals(name))
              .map(DeviceMapper::map)
              .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO create(DeviceDTO toAdd) {
        deviceDAO.save( DeviceMapper.map(toAdd));

        if (toAdd.getDeviceType().name()=="GENERATOR"){
            GeneratorTestDTO generatorTestDTO = new GeneratorTestDTO();
            generatorTestDTO.setAlerted(true);
            generatorTestDTO.setContent("[SYSTEM ENTRY - DO NOT REMOVE!]");
            generatorTestDTO.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
            generatorTestDTO.setInventNumber(toAdd.getInventNumber());
            generatorTestDTO.setStatus(Status.OK);
            generatorTestDTO.setLossPowerFlag(false);
            generatorTestDAO.save(GeneratorTestMapper.map(generatorTestDTO,deviceDAO));
        }

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

    @Override
    public DeviceDTO changeRoom(String inventNumber) {

       Device device = deviceDAO.findByInventNumber(inventNumber);


       if (!device.getDeviceType().equals(DeviceType.GENERATOR))  device.setDeviceDescription(device.getDeviceDescription()+ " ("+device.getRoom()+")");
       device.setRoom(Room.Nieaktywne);

        ShortPost shortPost = new ShortPost();
        shortPost.setAuthor(Author.DTN);
        shortPost.setDevice(device);
        shortPost.setContent("Urządzenie przeniesione do sekcji NIEAKTYWNE [SYSTEM]");
        shortPost.setPostLevel(PostLevel.INFO);
        shortPost.setPostDate(Calendar.getInstance());
        shortPost.setDate(Calendar.getInstance().getTime());
        shortPostDAO.save(shortPost);

       return DeviceMapper.map(deviceDAO.save(device));
    }
}
