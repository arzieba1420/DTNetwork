package com.arzieba.dtnetworkproject.services.device;

import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.DeviceCardDTO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.model.IssueDocument;


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




}
