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
    private Integer deviceId;


    private String deviceDescription;

    @OneToOne(mappedBy = "device")
    private DeviceCard deviceCard;

    @OneToMany(mappedBy = "device")
    private List<IssueDocument> issueDocumentList;

    @OneToMany(mappedBy = "device")
    private List<Damage> damageList;



}
