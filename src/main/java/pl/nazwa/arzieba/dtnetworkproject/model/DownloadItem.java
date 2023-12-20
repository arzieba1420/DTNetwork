package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DownloadItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;
    private String name;
    private String type;
}
