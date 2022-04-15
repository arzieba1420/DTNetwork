package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Component
@Getter
@Setter
public class DrycoolerSetDTO {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    private  Integer drycoolerSetId;
    private String inventNumber;
    private Author author;
    private String setDate;

    @Digits(integer = 10, fraction = 1,message = "Za dużo cyfr po przecinku")
    @DecimalMin("0.0")
    private Double actualSetPoint_CWL;
    private Double previousSetPoint_CWL;

    @Digits(integer = 10, fraction = 1,message = "Za dużo cyfr po przecinku")
    @DecimalMin("0.0")
    private Double actualSetPoint_CWR;
    private Double previousSetPoint_CWR;

    @Digits(integer = 10, fraction = 1,message = "Za dużo cyfr po przecinku")
    @DecimalMin("0.0")
    private Double actualSetPoint_AmbL;
    private Double previousSetPoint_AmbL; //temp zadana chillera

    @Digits(integer = 10, fraction = 1,message = "Za dużo cyfr po przecinku")
    @DecimalMin("0.0")

    private Double actualSetPoint_AmbR;
    private Double previousSetPoint_AmbR; //temp zadana chillera

    @Temporal(TemporalType.DATE)
    private String previousSetDate;
    private Author previousAuthor;
}
