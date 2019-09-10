package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;


import javax.mail.Multipart;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IssueDocument implements Serializable {

    private static final long serialVersionUID = 506172098L;

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

    @Transient
    @OneToMany(mappedBy = "issueDocument",fetch = FetchType.LAZY)
    private List<MultipartFile> files = new ArrayList<>();

    @Transient
    private List<String> filesToRemove = new ArrayList<>();
}
