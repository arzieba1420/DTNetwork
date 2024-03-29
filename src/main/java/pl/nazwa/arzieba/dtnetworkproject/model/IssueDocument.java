package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;
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

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    @Id
    private String issueSignature;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer issueId;
    private String delivererName;
    private String delivererNIP;

    @Temporal(TemporalType.DATE)
    private Calendar issueDate;
    private  String issueTittle;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String issueDetails;
    private String inventNumber;
    private double value;

    @ManyToOne
    @JoinColumn(name = "damage_Id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Damage damage;

    @Transient
    private List<MultipartFile> files;

    @Transient
    private List<String> filesToRemove;

    @OneToMany(mappedBy = "issueDocument")
    private List<IssueFiles> issueFiles = new ArrayList<>();
}
