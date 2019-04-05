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
    private String issueSignature;

    private String delivererName;
    private String delivererNIP;
    private Date issueDate;

    private String issueTittle;
    private String issueDetails;

    @ManyToOne
    @Column(nullable = true)
    @JoinColumn(name= "device_inventNumber")
    private Device device;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JoinColumn(name = "damage_damageId")
    private Damage damage;



}
