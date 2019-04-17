package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.DeviceType;
import com.arzieba.dtnetworkproject.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;



@Component
@Getter
@Setter
@ToString
public class DeviceCardDTO {

    private String address;
    private final String department = "DTN";
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

    private String deliveryDate;
    private String startDate;

    private String signatureNumber;
    private String financeSource;
    private String creatorOfDeviceCard;
    private String creationTime;
    private Double deviceValue;


    private String inventNumber;





}
