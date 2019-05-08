package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DeviceCard {
//cannot exist without Device


    @Id
    @GeneratedValue
    private Integer deviceCardID;

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

    @Temporal(TemporalType.DATE)
    private Calendar deliveryDate;

    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    private String signatureNumber;

    private String financeSource;

    private String creatorOfDeviceCard;

    @Temporal(TemporalType.DATE)
    private Calendar creationTime;

    private Double deviceValue;

    //To be matched with Device
    @OneToOne
    @JoinColumn(name = "Device_INVENT")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;



}


