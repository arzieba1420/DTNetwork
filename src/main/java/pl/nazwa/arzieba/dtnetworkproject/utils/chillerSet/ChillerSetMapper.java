package pl.nazwa.arzieba.dtnetworkproject.utils.chillerSet;

import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ChillerSetDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.ChillerSet;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

@Component
public class ChillerSetMapper {

    public static ChillerSet map (ChillerSetDTO dto, DeviceDAO dao){
        ChillerSet chillerSet = new ChillerSet();
        chillerSet.setAuthor(dto.getAuthor());
        chillerSet.setDevice(dao.findByInventNumber(dto.getInventNumber()));
        chillerSet.setSetDate(CalendarUtil.string2cal(dto.getSetDate()));
        chillerSet.setActualSetPoint(dto.getActualSetPoint());
        chillerSet.setPreviousSetPoint(dto.getPreviousSetPoint());
        chillerSet.setPreviousSetDate(CalendarUtil.string2cal(dto.getPreviousSetDate()));
        chillerSet.setPreviousAuthor(dto.getPreviousAuthor());
        return chillerSet;
    }

    public static ChillerSetDTO map (ChillerSet chillerSet ){
        ChillerSetDTO dto = new ChillerSetDTO();
        dto.setAuthor(chillerSet.getAuthor());
        dto.setInventNumber(chillerSet.getDevice().getInventNumber());
        dto.setSetDate(CalendarUtil.cal2string(chillerSet.getSetDate()));
        dto.setActualSetPoint(chillerSet.getActualSetPoint());
        dto.setChillerSetId(chillerSet.getChillerSetId());
        dto.setPreviousAuthor(chillerSet.getPreviousAuthor());
        dto.setPreviousSetDate(CalendarUtil.cal2string(chillerSet.getPreviousSetDate()));
        dto.setPreviousSetPoint(chillerSet.getPreviousSetPoint());
        return dto;
    }

}
