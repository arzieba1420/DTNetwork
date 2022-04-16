package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class IssueDocumentDTO {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

   @NotBlank(message = "Podaj nr sygnatury!")
    private String issueSignature;
    private String delivererName;
    private String delivererNIP;

   @Pattern(regexp="(^(19|[2-9][0-9])\\d\\d[\\-]((0[1-9]|1[012])[\\-]((0[1-9]|1[0-9]|2[0-8]))|((0[13578]|1[02])[\\-](29|30|31))|((0[4,6,9]|11)[\\-](29|30))))$|(^(19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)[\\-]02[\\-]29[\\-]$)",
            message = "Niewłaściwa data lub niepoprawny format!")
    private String issueDate;

   @NotBlank(message = "Podaj tytuł sprawy!")
    private String issueTittle;

   @NotBlank(message = "Podaj szczegóły zamówienia!")
    private String issueDetails;
    private Integer damageId;
    private String inventNumber;

   @Positive(message = "Value must be greater than 0")
   @Digits(integer = 10, fraction = 2,message = "Too many fraction numbers")
    private double value;
    private List<MultipartFile> issueFiles ;
    private List<String> filesToRemove;

    //-----------------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------


    public IssueDocumentDTO(String issueSignature, String delivererName, String delivererNIP, String issueDate, String issueTittle, String issueDetails, String inventNumber, double value) {
        this.issueSignature = issueSignature;
        this.delivererName = delivererName;
        this.delivererNIP = delivererNIP;
        this.issueDate = issueDate;
        this.issueTittle = issueTittle;
        this.issueDetails = issueDetails;
        this.inventNumber = inventNumber;
        this.value = value;
    }
}
