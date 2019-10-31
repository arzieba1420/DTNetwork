package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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

    @Transient
    private List<Author> authors;

    private String authorsCommaSeparated;

    private boolean lossPowerFlag;
    private boolean alerted;

}
