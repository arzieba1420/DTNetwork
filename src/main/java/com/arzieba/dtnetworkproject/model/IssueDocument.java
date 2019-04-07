package com.arzieba.dtnetworkproject.model;

import lombok.*;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
public class IssueDocument {

    @Id
    private String issueSignature;

    private String delivererName;
    private String delivererNIP;
    private Date issueDate;

    private  String issueTittle;
    private String issueDetails;

    @ManyToOne
    @JoinColumn(name= "device_inventNumber")
    private Device device;

    @ManyToOne()
    @JoinColumn(name = "damage_damageId")
    private Damage damage;

    public String getIssueSignature() {
        return issueSignature;
    }

    public void setIssueSignature(String issueSignature) {
        this.issueSignature = issueSignature;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    public String getDelivererNIP() {
        return delivererNIP;
    }

    public void setDelivererNIP(String delivererNIP) {
        this.delivererNIP = delivererNIP;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueTittle() {
        return issueTittle;
    }

    public void setIssueTittle(String issueTittle) {
        this.issueTittle = issueTittle;
    }

    public String getIssueDetails() {
        return issueDetails;
    }

    public void setIssueDetails(String issueDetails) {
        this.issueDetails = issueDetails;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }
}
