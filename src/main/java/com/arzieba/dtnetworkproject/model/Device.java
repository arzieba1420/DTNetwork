package com.arzieba.dtnetworkproject.model;

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

    private String deviceDescription;



    @OneToOne(mappedBy = "device")
    private DeviceCard deviceCard;



    @OneToMany(mappedBy = "device", fetch =FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Damage> damageList;

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
                ", deviceCard=" + deviceCard +
                ", damageList=" + damageList +
                '}';
    }
}
