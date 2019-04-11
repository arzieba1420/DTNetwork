package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DamageDTO {


    //****Fields

    private String description;


    private String damageDate;

    private String Author;

    private String deviceInventNumber; //is deviceDescription in Device.class

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

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getDeviceInventNumber() {
        return deviceInventNumber;
    }

    public void setDeviceInventNumber(String deviceInventNumber) {
        this.deviceInventNumber = deviceInventNumber;
    }

    @Override
    public String toString() {
        return "DamageDTO{" +
                "description='" + description + '\'' +
                ", damageDate='" + damageDate + '\'' +
                ", Author='" + Author + '\'' +
                ", deviceInventNumber='" + deviceInventNumber + '\'' +
                '}';
    }
}

    //*******Additional Util Methods

    /*//Mapper to Domain


    }
}*/
