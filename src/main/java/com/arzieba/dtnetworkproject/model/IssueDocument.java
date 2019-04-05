package com.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
public class IssueDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Getter
    private Integer issueDocumentId;

    private String delivererName;
    private String delivererNIP;
    private Date issueDate;
    private String issueSignature;
    private String issueTittle;
    private String issueDetails;

    @ManyToOne
    @Column(nullable = true)
    @JoinColumn(name= "device_deviceId")
    private Device device;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JoinColumn(name = "damage_damageId")
    private Damage damage;



}
