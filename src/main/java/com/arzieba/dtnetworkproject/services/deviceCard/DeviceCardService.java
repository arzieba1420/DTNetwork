package com.arzieba.dtnetworkproject.services.deviceCard;

import com.arzieba.dtnetworkproject.dto.DeviceCardDTO;

import java.util.List;

public interface DeviceCardService {

    List<DeviceCardDTO> findAll();
    List<String> showAllSignatures();
    DeviceCardDTO findForDevice(String inv);
    DeviceCardDTO findForSignature(String signature);
    DeviceCardDTO findForId(Integer id);
    String create(DeviceCardDTO dto);

}
