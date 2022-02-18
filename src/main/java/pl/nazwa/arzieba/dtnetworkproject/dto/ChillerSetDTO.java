package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.util.Calendar;

@Component
@Getter
@Setter
public class ChillerSetDTO {

    private Integer chillerSetId;
    private Author author;
    private String inventNumber;
    private String setDate;
    @Digits(integer = 10, fraction = 1,message = "Za dużo cyfr po przecinku")
    @DecimalMin("0.0")
    private Double actualSetPoint;
    private Double previousSetPoint; //temp zadana chillera
    @Temporal(TemporalType.DATE)
    private String previousSetDate;
    private Author previousAuthor;
}
