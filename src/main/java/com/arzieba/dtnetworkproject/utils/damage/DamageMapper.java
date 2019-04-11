package com.arzieba.dtnetworkproject.utils.damage;

import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DamageMapper {

    //Mapper to DTO
    public static DamageDTO map(Damage damage){
        DamageDTO damageDTO = new DamageDTO();
        damageDTO.setDescription(damage.getDescription());
        damageDTO.setDamageDate(CalendarUtil.cal2string(damage.getDamageDate()));
        damageDTO.setAuthor(damage.getAuthor());
        damageDTO.setDeviceInventNumber(damage.getDevice().getInventNumber());
        return damageDTO;
    }

    public static   Damage map(DamageDTO damageDTO, DeviceDAO deviceDAO){

        Damage damage = new Damage();
        damage.setDamageDate(CalendarUtil.string2cal(damageDTO.getDamageDate()));
        damage.setAuthor(damageDTO.getAuthor());
        damage.setDevice(deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()));
        return damage;
    }





}
