package com.arzieba.dtnetworkproject.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IssueDocument {

    @Id
    private String issueSignature;

    private String delivererName;
    private String delivererNIP;

    @Temporal(TemporalType.DATE)
    private Calendar issueDate;

    private  String issueTittle;
    private String issueDetails;
    private String inventNumber;
    private double value;

    @ManyToOne
    @JoinColumn(name = "damage_Id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Damage damage;
}
