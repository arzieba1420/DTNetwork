package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Component
@Getter
@Setter
public class IssueDocumentDTO {


    @NotBlank(
            message = "Description cannot be empty!"  )
    private String issueSignature;
    private String delivererName;
    private String delivererNIP;

    @Pattern(regexp="(^(((0[1-9]|1[0-9]|2[0-8])[\\-](0[1-9]|1[012]))|((29|30|31)[\\-](0[13578]|1[02]))|((29|30)[\\-](0[4,6,9]|11)))[\\-](19|[2-9][0-9])\\d\\d$)|(^29[\\-]02[\\-](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)",
            message = "Invalid date or pattern: dd-MM-yyyy not satisfied!")
    private String issueDate;

    @NotBlank(
            message = "Description cannot be empty!"  )
    private String issueTittle;

    @NotBlank(
            message = "Description cannot be empty!"  )
    private String issueDetails;
    private Integer damageId;
    private String inventNumber;
    private double value;


}
