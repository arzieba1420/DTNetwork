package pl.nazwa.arzieba.dtnetworkproject.services.device;

import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;


import java.util.List;

public interface DeviceService {

     List<DeviceDTO> findAll();
     DeviceDTO findByInventNumber(String inventNumber);
     List<DeviceDTO> findByType(String name);
     DeviceDTO create(DeviceDTO toAdd);
     DeviceDTO update(DeviceDTO deviceDTO);
     DeviceDTO remove(String inventNumber);
     List<DamageDTO> getDamages (String inventNumber);
     List<IssueDocumentDTO> getIssueDocuments (String inventNumber);
     DeviceCardDTO getDeviceCard(String inventNumber);
     List<DeviceDTO> findByRoom(String room);
}
