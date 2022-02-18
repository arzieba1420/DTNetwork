package pl.nazwa.arzieba.dtnetworkproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ElectricalInvoice {

    @Id
    private String invoiceId;
    private Calendar date;
    private BuildingType building;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double netValue;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double grossValue;
}
