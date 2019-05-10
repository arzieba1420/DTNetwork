package pl.nazwa.arzieba.dtnetworkproject.utils.device;

import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
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
