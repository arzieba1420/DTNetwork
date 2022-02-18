package pl.nazwa.arzieba.dtnetworkproject.utils.drycoolerSet;

import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DrycoolerSetDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.DrycoolerSet;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

@Component
public class DrycoolerSetMapper {

    public  static DrycoolerSet map (DrycoolerSetDTO dto, DeviceDAO dao){

        DrycoolerSet drycoolerSet = new DrycoolerSet();

        drycoolerSet.setActualSetPoint_AmbL(dto.getActualSetPoint_AmbL());
        drycoolerSet.setActualSetPoint_AmbR(dto.getActualSetPoint_AmbR());
        drycoolerSet.setActualSetPoint_CWL(dto.getActualSetPoint_CWL());
        drycoolerSet.setActualSetPoint_CWR(dto.getActualSetPoint_CWR());
        drycoolerSet.setPreviousSetPoint_AmbL(dto.getPreviousSetPoint_AmbL());
        drycoolerSet.setPreviousSetPoint_AmbR(dto.getPreviousSetPoint_AmbR());
        drycoolerSet.setPreviousSetPoint_CWL(dto.getPreviousSetPoint_CWL());
        drycoolerSet.setPreviousSetPoint_CWR(dto.getPreviousSetPoint_CWR());
        drycoolerSet.setAuthor(dto.getAuthor());
        drycoolerSet.setPreviousAuthor(dto.getPreviousAuthor());
        drycoolerSet.setDevice(dao.findByInventNumber(dto.getInventNumber()));
        drycoolerSet.setDrycoolerSetId(dto.getDrycoolerSetId());
        drycoolerSet.setSetDate(CalendarUtil.string2cal(dto.getSetDate()));
        drycoolerSet.setPreviousSetDate(CalendarUtil.string2cal(dto.getPreviousSetDate()));

        return drycoolerSet;
    }

    public static DrycoolerSetDTO map (DrycoolerSet drycoolerSet){

        DrycoolerSetDTO dto = new DrycoolerSetDTO();

        dto.setActualSetPoint_AmbL(drycoolerSet.getActualSetPoint_AmbL());
        dto.setActualSetPoint_AmbR(drycoolerSet.getActualSetPoint_AmbR());
        dto.setActualSetPoint_CWL(drycoolerSet.getActualSetPoint_CWL());
        dto.setActualSetPoint_CWR(drycoolerSet.getActualSetPoint_CWR());
        dto.setAuthor(drycoolerSet.getAuthor());
        dto.setDrycoolerSetId(drycoolerSet.getDrycoolerSetId());
        dto.setInventNumber(drycoolerSet.getDevice().getInventNumber());
        dto.setPreviousAuthor(drycoolerSet.getPreviousAuthor());
        dto.setPreviousSetDate(CalendarUtil.cal2string(drycoolerSet.getPreviousSetDate()));
        dto.setSetDate(CalendarUtil.cal2string(drycoolerSet.getSetDate()));
        dto.setPreviousSetPoint_AmbL(drycoolerSet.getPreviousSetPoint_AmbL());
        dto.setPreviousSetPoint_AmbR(drycoolerSet.getPreviousSetPoint_AmbR());
        dto.setPreviousSetPoint_CWL(drycoolerSet.getPreviousSetPoint_CWL());
        dto.setPreviousSetPoint_CWR(drycoolerSet.getPreviousSetPoint_CWR());

        return dto;
    }
}
