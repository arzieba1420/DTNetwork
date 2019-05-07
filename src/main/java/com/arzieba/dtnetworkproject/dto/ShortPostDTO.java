package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Author;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;

@Component
@Getter
@Setter
public class ShortPostDTO {

    private Author author;
    private String content;

    @Pattern(regexp="^\\d{2}-\\d{2}-\\d{4}$", message = "Respect pattern dd-MM-yyyy")
    private String date;
    private String inventNumber;
    private boolean isForDamage = false;
}
