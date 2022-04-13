package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortPostDTO {

    @NotNull(message = "Podaj autora posta!")
    private Author author;
    @NotBlank(message = "Podaj treść posta!"  )
    private String content;
    @Pattern(regexp="(^(19|[2-9][0-9])\\d\\d[\\-]((0[1-9]|1[012])[\\-]((0[1-9]|1[0-9]|2[0-8]))|((0[13578]|1[02])[\\-](29|30|31))|((0[4,6,9]|11)[\\-](29|30))))$|(^(19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)[\\-]02[\\-]29[\\-]$)",
            message = "Niewłaściwa data lub niepoprawny format!")
    private String date;
    @NotNull(message = "Podaj urządzenie lub wybierz Inne->DTN!")
    private String inventNumber;
    private boolean isForDamage = false;
    private PostLevel postLevel = PostLevel.GENERAL;
}
