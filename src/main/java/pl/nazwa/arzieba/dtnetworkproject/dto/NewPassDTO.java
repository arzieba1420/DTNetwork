package pl.nazwa.arzieba.dtnetworkproject.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Component
@Getter
@Setter
public class NewPassDTO {

    @NotBlank
    @Size(min = 6,message = "Hasło za krótkie: minimum 6 znaków")
    private String newPass;

    @NotBlank(message = "Pole nie może być puste")
    private String newPassConfirmed;

    private String login;

    @NotBlank(message = "Pole nie może być puste")
    private String oldPass;



}
