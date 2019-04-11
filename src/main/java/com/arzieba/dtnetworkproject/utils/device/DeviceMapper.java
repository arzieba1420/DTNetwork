package com.arzieba.dtnetworkproject.utils.device;

import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public static DeviceDTO map(Device device){
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setRoom(device.getRoom());
        deviceDTO.setDeviceDescription(device.getDeviceDescription());
        deviceDTO.setInventNumber(device.getInventNumber());
        deviceDTO.setDeviceType(device.getDeviceType());
        return deviceDTO;
    }
    public static Device map(DeviceDTO deviceDTO){
        Device device = new Device();
        device.setInventNumber(deviceDTO.getInventNumber());
        device.setDeviceDescription(deviceDTO.getDeviceDescription());
        device.setRoom(deviceDTO.getRoom());
        device.setDeviceType(deviceDTO.getDeviceType());
        return device;
    }

}
