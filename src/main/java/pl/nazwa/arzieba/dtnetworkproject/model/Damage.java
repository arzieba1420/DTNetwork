package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Damage {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    //cannot exist without Device
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer damageId;

    @ManyToOne
    @JoinColumn(name = "device_inventNumber")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Device device;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Temporal(TemporalType.DATE)
    private Calendar damageDate;
    private Author author;

    @OneToMany(mappedBy = "damage", fetch =FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IssueDocument> issueDocumentList;
}
