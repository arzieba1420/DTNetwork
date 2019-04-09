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

    private String deviceDescription; //is DeviceDescription in Device.class

    //**** Getters, setters, toString, constructor


    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

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

    @Override
    public String toString() {
        return "DamageDTO{" +
                "description='" + description + '\'' +
                ", damageDate=" + damageDate +
                ", Author='" + Author + '\'' +
                ", deviceDescription='" + deviceDescription + '\'' +
                '}';
    }

    //*******Additional Util Methods

    //Mapper to Domain
    public static   Damage mapper(DamageDTO damageDTO, DeviceDAO deviceDAO){



        Damage damage = new Damage();


        damage.setDamageDate(DamageDTO.getCalendar(damageDTO.getDamageDate()));
        damage.setAuthor(damageDTO.getAuthor());
        damage.setDevice(deviceDAO.findByDeviceDescription(damageDTO.getDeviceDescription()));

        return damage;
    }

    //Parse to Calendar
    private static Calendar getCalendar(String dateToParse) {
        String stringDate = dateToParse;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date date;
        {
            try {
               date  = formatter.parse(stringDate);
                Calendar calender = Calendar.getInstance();
                calender.setTime(date);

                return calender;
            } catch (ParseException e) {
               e.getMessage();
            }
            return null;
        }

    }
}
