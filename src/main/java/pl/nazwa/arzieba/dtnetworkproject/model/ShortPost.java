package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Id
    @GeneratedValue
    private Integer postId;

    private Author author;
    private String content;

    @Temporal(TemporalType.DATE)
    private Calendar postDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = postDate==null? Calendar.getInstance().getTime() : postDate.getTime();

    @ManyToOne
    @JoinColumn(name ="Device_INVENT")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;

}
