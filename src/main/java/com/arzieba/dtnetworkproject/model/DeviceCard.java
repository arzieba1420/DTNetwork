package com.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
public class DeviceCard {
//cannot exist without Device


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer deviceCardID;

    private String address;

    private final String Department = "DTN";

    private Room installPlace;

    private String inventNumber;

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
    @Column(nullable = false)
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "device_deviceId")
    private Device device;


}


