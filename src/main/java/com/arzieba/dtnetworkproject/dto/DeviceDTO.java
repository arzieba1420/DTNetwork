package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.model.DeviceType;
import com.arzieba.dtnetworkproject.model.Room;
import org.springframework.stereotype.Component;

@Component
public class DeviceDTO {

    private String inventNumber;
    private String deviceDescription;
    private Room room;
    private DeviceType deviceType;


    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getInventNumber() {
        return inventNumber;
    }

    public void setInventNumber(String inventNumber) {
        this.inventNumber = inventNumber;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public static Device mapper(DeviceDTO deviceDTO){
        Device device = new Device();
        device.setInventNumber(deviceDTO.getInventNumber());
        device.setDeviceDescription(deviceDTO.getDeviceDescription());
        device.setRoom(deviceDTO.getRoom());
        device.setDeviceType(deviceDTO.getDeviceType());
        return device;
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
                "inventNumber='" + inventNumber + '\'' +
                ", deviceDescription='" + deviceDescription + '\'' +
                ", room=" + room +
                ", deviceType=" + deviceType +
                '}';
    }
}
