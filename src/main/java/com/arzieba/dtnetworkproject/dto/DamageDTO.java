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


    //****Fields

    private String description;


    @Pattern(regexp="dd-MM-yyyy")
    private String damageDate;

    private Author author;

    private String deviceInventNumber; //is deviceDescription in Device.class

    private Integer damageId;

    private boolean newPostFlag;

    //**** Getters, setters, toString, constructor


}

    //*******Additional Util Methods

    /*//Mapper to


    }
}*/
