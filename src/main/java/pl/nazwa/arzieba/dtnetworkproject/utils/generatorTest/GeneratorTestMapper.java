package pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest;


import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.GeneratorTest;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

@Component
public class GeneratorTestMapper {

    public static GeneratorTest map(GeneratorTestDTO dto,DeviceDAO deviceDAO) {

        GeneratorTest generatorTest = new GeneratorTest();
        generatorTest.setContent(dto.getContent());
        generatorTest.setDate(CalendarUtil.string2cal(dto.getDate()));
        generatorTest.setDevice(deviceDAO.findByInventNumber(dto.getInventNumber()));
        generatorTest.setForLoss(dto.isForLoss());
        generatorTest.setStatus(dto.getStatus());
        return generatorTest;
    }

    public static GeneratorTestDTO map(GeneratorTest test){

        GeneratorTestDTO dto = new GeneratorTestDTO();
        dto.setContent(test.getContent());
        dto.setDate(CalendarUtil.cal2string(test.getDate()));
        dto.setForLoss(test.isForLoss());
        dto.setInventNumber(test.getDevice().getInventNumber());
        dto.setStatus(test.getStatus());
        return dto;
    }

}
