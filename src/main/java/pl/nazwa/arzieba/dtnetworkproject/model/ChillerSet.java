package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Entity
@Table
@Getter
@Setter
@ToString

public class ChillerSet {

    @Id
    @GeneratedValue
    Integer chillerSetId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Device_INVENT")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;

    private Double actualSetPoint; //temp zadana chillera

    @Temporal(TemporalType.DATE)
    private Calendar setDate;

    private Author author;

    private Double previousSetPoint; //temp zadana chillera

    @Temporal(TemporalType.DATE)
    private Calendar previousSetDate;

    private Author previousAuthor;

    public ChillerSet() {
    }
}
