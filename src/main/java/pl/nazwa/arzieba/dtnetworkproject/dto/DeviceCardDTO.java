package pl.nazwa.arzieba.dtnetworkproject.dto;

import pl.nazwa.arzieba.dtnetworkproject.model.DeviceType;
import pl.nazwa.arzieba.dtnetworkproject.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;


@Component
@Getter
@Setter
@ToString
public class DeviceCardDTO {

    private  String parameters ;
    private final String department = "DTN";
    private Room installPlace;
    private String keeperData;
    private String deviceName;
    private DeviceType deviceType;
    private String fabricalID;
    private String producer;
    private String deliverer;

    @DecimalMax(value = "2100", message = "Incorrect year: out of bounds <1970;2100>") @DecimalMin(value = "1970",message = "Incorrect year: out of bounds <1970;2100>")
    private int buildTime;
    private String deliveryDocumentID;
    private String attachementsIDs;

    @Pattern(regexp="(^(19|[2-9][0-9])\\d\\d[\\-]((0[1-9]|1[012])[\\-](0[1-9]|1[0-9]|2[0-8]))|((0[13578]|1[02])[\\-](29|30|31))|((0[4,6,9]|11)[\\-](29|30)))$|(^(19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)[\\-]02[\\-]29[\\-]$)",
            message = "Invalid date or pattern: yyyy-MM-dd not satisfied!")
    private String deliveryDate;

    @Pattern(regexp="(^(19|[2-9][0-9])\\d\\d[\\-]((0[1-9]|1[012])[\\-](0[1-9]|1[0-9]|2[0-8]))|((0[13578]|1[02])[\\-](29|30|31))|((0[4,6,9]|11)[\\-](29|30)))$|(^(19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)[\\-]02[\\-]29[\\-]$)",
            message = "Invalid date or pattern: yyyy-MM-dd not satisfied!")
    private String startDate;
    private String signatureNumber;
    private String financeSource;
    private String creatorOfDeviceCard;

    @Pattern(regexp="(^(19|[2-9][0-9])\\d\\d[\\-]((0[1-9]|1[012])[\\-](0[1-9]|1[0-9]|2[0-8]))|((0[13578]|1[02])[\\-](29|30|31))|((0[4,6,9]|11)[\\-](29|30)))$|(^(19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)[\\-]02[\\-]29[\\-]$)",
            message = "Invalid date or pattern: yyyy-MM-dd not satisfied!")
    private String creationTime;
    private Double deviceValue;
    private String inventNumber;
}
