package com.arzieba.dtnetworkproject.model;

import com.arzieba.dtnetworkproject.dto.DamageDTO;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.Calendar;
import java.util.List;


@Entity
@Table
public class Damage {
    //cannot exist without Device

    @Id
    @GeneratedValue
    private Integer damageId;

    @ManyToOne
    @JoinColumn(name = "device_inventNumber")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Device device;

    private String description;
    @Temporal(TemporalType.DATE)
    private Calendar damageDate;
    private String author;

    @OneToMany(mappedBy = "damage", fetch =FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IssueDocument> issueDocumentList;

    public Damage() {
    }

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

    public Calendar getDamageDate() {
        return damageDate;
    }

    public void setDamageDate(Calendar damageDate) {
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
