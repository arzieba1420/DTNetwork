package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Damage;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Calendar;

@Component
@Getter
@Setter
public class IssueDocumentDTO {


    private String issueSignature;
    private String delivererName;
    private String delivererNIP;

    @Pattern(regexp="^\\d{2}-\\d{2}-\\d{4}$")
    private String issueDate;
    private String issueTittle;
    private String issueDetails;
    private Integer damageId;
    private String inventNumber;
    private double value;


}
