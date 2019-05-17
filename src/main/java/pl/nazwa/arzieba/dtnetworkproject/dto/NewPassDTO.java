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
    @Size(min = 8,message = "Password is too short: min 8 chars")
    private String newPass;

    @NotBlank(message = "Field cannot be empty")
    private String newPassConfirmed;

    private String login;

    @NotBlank(message = "Field cannot be empty")
    private String oldPass;



}
