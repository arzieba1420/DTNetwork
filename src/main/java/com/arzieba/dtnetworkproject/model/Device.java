package com.arzieba.dtnetworkproject.model;

import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.annotation.Primary;


import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Device {

    @Id
    private String inventNumber;

    @Column(nullable = false)
    private String deviceDescription;

    @Column(nullable = false)
    private Room room;

    @Column(nullable = false)
    private DeviceType deviceType;



    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @OneToOne(mappedBy = "device")
    private DeviceCard deviceCard;

    @OneToMany(mappedBy = "device", fetch =FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Damage> damageList;

    public Device() {
    }

    public DeviceType getDeviceType() {
        return deviceType;
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

    public DeviceCard getDeviceCard() {
        return deviceCard;
    }

    public void setDeviceCard(DeviceCard deviceCard) {
        this.deviceCard = deviceCard;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Damage> getDamageList() {
        return damageList;
    }

    public void setDamageList(List<Damage> damageList) {
        this.damageList = damageList;
    }

    @Override
    public String toString() {
        return "Device{" +
                "inventNumber='" + inventNumber + '\'' +
                ", deviceDescription='" + deviceDescription + '\'' +
                ", room=" + room +
                ", deviceType=" + deviceType +
                ", deviceCard=" + deviceCard +
                ", damageList=" + damageList +
                '}';
    }

    public static DeviceDTO mapper(Device device){
      DeviceDTO deviceDTO = new DeviceDTO();
      deviceDTO.setRoom(device.getRoom());
      deviceDTO.setDeviceDescription(device.getDeviceDescription());
      deviceDTO.setInventNumber(device.getInventNumber());
      deviceDTO.setDeviceType(device.getDeviceType());
      return deviceDTO;
    };
}
