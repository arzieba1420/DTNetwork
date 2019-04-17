package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Author;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
public class DamageDTO {


    //****Fields

    private String description;


    private String damageDate;

    private Author author;

    private String deviceInventNumber; //is deviceDescription in Device.class

    private Integer damageId;

    //**** Getters, setters, toString, constructor


}

    //*******Additional Util Methods

    /*//Mapper to Domain


    }
}*/
