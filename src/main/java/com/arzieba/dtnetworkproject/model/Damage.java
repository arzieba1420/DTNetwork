package com.arzieba.dtnetworkproject.model;

import lombok.*;


import javax.persistence.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Setter
@Getter
@Entity
@Table
public class Damage {
    //cannot exist without Device

    @Id
    @GeneratedValue
    private Integer damageId;

    @ManyToOne
    @JoinColumn(name = "device_inventNumber")
    private Device device;

    private String description;
    private GregorianCalendar damageDate;
    private String author;

    @OneToMany(mappedBy = "damage")
    private List<IssueDocument> issueDocumentList;


    public Integer getDamageId() {
        return damageId;
    }

    public void setDamageId(Integer damageId) {
        this.damageId = damageId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getDamageDate() {
        return damageDate;
    }

    public void setDamageDate(GregorianCalendar damageDate) {
        this.damageDate = damageDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<IssueDocument> getIssueDocumentList() {
        return issueDocumentList;
    }

    public void setIssueDocumentList(List<IssueDocument> issueDocumentList) {
        this.issueDocumentList = issueDocumentList;
    }
}
