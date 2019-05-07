package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Author;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;

@Component
@Getter
@Setter
@ToString
public class DamageDTO {

    private String description;

    @Pattern(regexp="^\\d{2}-\\d{2}-\\d{4}$")
    private String damageDate;

    private Author author;

    private String deviceInventNumber;

    private Integer damageId;

    private boolean newPostFlag;  //auto-create relate shortPost or not

}


