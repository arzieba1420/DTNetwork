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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long issueFilesId;
    private String fileName;
    private String modifiedFileName;
    private String fileExtension;

    @ManyToOne
    @JoinColumn(name = "issueSignature")
    private IssueDocument issueDocument;

}
