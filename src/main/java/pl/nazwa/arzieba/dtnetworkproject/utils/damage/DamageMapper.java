package pl.nazwa.arzieba.dtnetworkproject.utils.damage;

import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.stereotype.Component;

@Component
public class DamageMapper {

    //Mapper to DTO
    public static DamageDTO map(Damage damage){
        DamageDTO damageDTO = new DamageDTO();
        damageDTO.setDescription(damage.getDescription());
        damageDTO.setDamageDate(CalendarUtil.cal2string(damage.getDamageDate()));
        damageDTO.setAuthor(damage.getAuthor());

        if(damage.getDevice()!=null){
        damageDTO.setDeviceInventNumber(damage.getDevice().getInventNumber());}
        else damageDTO.setDeviceInventNumber(null);
        
        damageDTO.setDamageId(damage.getDamageId());
        return damageDTO;
    }

    public static   Damage map(DamageDTO damageDTO,DeviceDAO deviceDAO){

        Damage damage = new Damage();
        damage.setDamageDate(CalendarUtil.string2cal(damageDTO.getDamageDate()));
        damage.setAuthor(damageDTO.getAuthor());
        damage.setDevice(deviceDAO.findByInventNumber(damageDTO.getDeviceInventNumber()));
        damage.setDescription(damageDTO.getDescription());
        return damage;
    }





}
