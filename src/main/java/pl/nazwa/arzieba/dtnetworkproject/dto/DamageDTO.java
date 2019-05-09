package pl.nazwa.arzieba.dtnetworkproject.dto;

import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Component
@Getter
@Setter
public class DamageDTO {

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$",
            message = "Damage description cannot be empty!"  )
    private String description;

    @Pattern(regexp="(^(((0[1-9]|1[0-9]|2[0-8])[\\-](0[1-9]|1[012]))|((29|30|31)[\\-](0[13578]|1[02]))|((29|30)[\\-](0[4,6,9]|11)))[\\-](19|[2-9][0-9])\\d\\d$)|(^29[\\-]02[\\-](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)",
            message = "Invalid date or pattern: dd-MM-yyyy not satisfied!")
    private String damageDate;

    @NotNull(message = "Author not specified!")
    private Author author;

    private String deviceInventNumber;

    private Integer damageId;

    private boolean newPostFlag;  //auto-create relate shortPost or not

}


