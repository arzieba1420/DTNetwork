package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Author;
import org.springframework.stereotype.Component;

@Component
public class DamageDTO {


    //****Fields

    private String description;


    private String damageDate;

    private Author author;

    private String deviceInventNumber; //is deviceDescription in Device.class

    private Integer damageId;

    //**** Getters, setters, toString, constructor


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDamageDate() {
        return damageDate;
    }

    public void setDamageDate(String damageDate) {
        this.damageDate = damageDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDeviceInventNumber() {
        return deviceInventNumber;
    }

    public void setDeviceInventNumber(String deviceInventNumber) {
        this.deviceInventNumber = deviceInventNumber;
    }

    public Integer getDamageId() {
        return damageId;
    }

    public void setDamageId(Integer damageId) {
        this.damageId = damageId;
    }

    @Override
    public String toString() {
        return "DamageDTO{" +
                "description='" + description + '\'' +
                ", damageDate='" + damageDate + '\'' +
                ", author='" + author + '\'' +
                ", deviceInventNumber='" + deviceInventNumber + '\'' +
                ", damageId=" + damageId +
                '}';
    }
}

    //*******Additional Util Methods

    /*//Mapper to Domain


    }
}*/
