package com.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table
public class Damage {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer damageId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "device_deviceId")
    private Device device;

    private String description;
    private Date damageDate;
    private String author;

    @OneToMany(mappedBy = "damage")
    private List<IssueDocument> issueDocumentList;




}
