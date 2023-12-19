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
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShortPost {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private Author author;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Temporal(TemporalType.DATE)
    private Calendar postDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = Calendar.getInstance().getTime();

    @ManyToOne
    @JoinColumn(name ="Device_INVENT")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;
    private PostLevel postLevel = PostLevel.GENERAL;

}
