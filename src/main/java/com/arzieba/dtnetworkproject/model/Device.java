package com.arzieba.dtnetworkproject.model;

import lombok.*;
import org.springframework.context.annotation.Primary;


import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Device {

    @Id
    private String inventNumber;

    private String deviceDescription;

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DeviceCard deviceCard;

    @OneToMany(mappedBy = "device")
    private List<IssueDocument> issueDocumentList;

    @OneToMany(mappedBy = "device")
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

    public List<IssueDocument> getIssueDocumentList() {
        return issueDocumentList;
    }

    public void setIssueDocumentList(List<IssueDocument> issueDocumentList) {
        this.issueDocumentList = issueDocumentList;
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
                ", issueDocumentList=" + issueDocumentList +
                ", damageList=" + damageList +
                '}';
    }
}
