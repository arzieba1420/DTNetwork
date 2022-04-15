package pl.nazwa.arzieba.dtnetworkproject.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class IssueFiles implements Serializable {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueFilesId;
    private String fileName;
    private String fileNameNoExt;
    private String modifiedFileName;
    private String fileExtension;

    @ManyToOne
    @JoinColumn(name="issue_signature")
    private IssueDocument issueDocument;
}
