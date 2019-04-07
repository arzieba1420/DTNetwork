package com.arzieba.dtnetworkproject.model;

import lombok.*;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table

public class DeviceCard {
//cannot exist without Device


    @Id
    @GeneratedValue
    private Integer deviceCardID;

    private String address;

    private final String Department = "DTN";

    private Room installPlace;



    private String keeperData;

    private String deviceName;

    private DeviceType deviceType;

    private String fabricalID;

    private String producer;

    private String deliverer;

    private int buildTime;

    private String deliveryDocumentID;

    private String attachementsIDs;

    private Date deliveryDate;

    private Date startDate;

    private String signatureNumber;

    private String financeSource;

    private String creatorOfDeviceCard;

    private Date creationTime;

    private Double deviceValue;

    //To be matched with Device

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "device_serialNumber")
    private Device device;

    public Integer getDeviceCardID() {
        return deviceCardID;
    }

    public void setDeviceCardID(Integer deviceCardID) {
        this.deviceCardID = deviceCardID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return Department;
    }

    public Room getInstallPlace() {
        return installPlace;
    }

    public void setInstallPlace(Room installPlace) {
        this.installPlace = installPlace;
    }

    public String getKeeperData() {
        return keeperData;
    }

    public void setKeeperData(String keeperData) {
        this.keeperData = keeperData;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getFabricalID() {
        return fabricalID;
    }

    public void setFabricalID(String fabricalID) {
        this.fabricalID = fabricalID;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public int getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(int buildTime) {
        this.buildTime = buildTime;
    }

    public String getDeliveryDocumentID() {
        return deliveryDocumentID;
    }

    public void setDeliveryDocumentID(String deliveryDocumentID) {
        this.deliveryDocumentID = deliveryDocumentID;
    }

    public String getAttachementsIDs() {
        return attachementsIDs;
    }

    public void setAttachementsIDs(String attachementsIDs) {
        this.attachementsIDs = attachementsIDs;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSignatureNumber() {
        return signatureNumber;
    }

    public void setSignatureNumber(String signatureNumber) {
        this.signatureNumber = signatureNumber;
    }

    public String getFinanceSource() {
        return financeSource;
    }

    public void setFinanceSource(String financeSource) {
        this.financeSource = financeSource;
    }

    public String getCreatorOfDeviceCard() {
        return creatorOfDeviceCard;
    }

    public void setCreatorOfDeviceCard(String creatorOfDeviceCard) {
        this.creatorOfDeviceCard = creatorOfDeviceCard;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Double getDeviceValue() {
        return deviceValue;
    }

    public void setDeviceValue(Double deviceValue) {
        this.deviceValue = deviceValue;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}


