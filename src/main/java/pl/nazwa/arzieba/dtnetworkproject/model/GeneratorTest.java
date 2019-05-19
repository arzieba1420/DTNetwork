package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GeneratorTest {

    @Id
    @GeneratedValue
    private Integer testId;

    @ManyToOne
    @JoinColumn(name ="Device_INVENT")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;

    @Temporal(TemporalType.DATE)
    private Calendar date;
    private String content;
    private Status status;
    private boolean isForLoss;

}
