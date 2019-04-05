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
    //cannot exist without Device

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer damageId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "device_inventNumber")
    @Column(nullable = false)
    private Device device;

    private String description;
    private Date damageDate;
    private String author;

    @OneToMany(mappedBy = "damage")
    private List<IssueDocument> issueDocumentList;




}
