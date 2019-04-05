package com.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Setter
@Getter
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Getter
    @Setter
    private Integer deviceId;
    private DeviceType deviceType;
    private Room room;
    private String deviceDescription;
    @OneToOne(mappedBy = "DeviceCard_device")
    private DeviceCard deviceCard;

    @OneToMany(mappedBy = "IssueDocument_device")
    private List<IssueDocument> issueDocumentList;

    @OneToMany(mappedBy = "damage_device")
    private List<Damage> damageList;



}
