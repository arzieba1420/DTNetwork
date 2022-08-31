package pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest;


import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.GeneratorTest;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class GeneratorTestMapper {

    public static GeneratorTest map(GeneratorTestDTO dto,DeviceDAO deviceDAO) {
        GeneratorTest generatorTest = new GeneratorTest();
        generatorTest.setContent(dto.getContent());
        generatorTest.setDate(CalendarUtil.string2cal(dto.getDate()));
        generatorTest.setDevice(deviceDAO.findByInventNumber(dto.getInventNumber()));
        generatorTest.setLossPowerFlag(dto.isLossPowerFlag());
        generatorTest.setStatus(dto.getStatus());
        generatorTest.setAlerted(dto.isAlerted());

        if(dto.getAuthors()==null)
            generatorTest.setAuthors(Arrays.asList(Author.DTP));
        else generatorTest.setAuthors(dto.getAuthors().stream().map(a-> Author.valueOf(a)).collect(Collectors.toList()));
        return generatorTest;
    }

    public static GeneratorTestDTO map(GeneratorTest test){
        GeneratorTestDTO dto = new GeneratorTestDTO();
        dto.setContent(test.getContent());
        dto.setDate(CalendarUtil.cal2string(test.getDate()));
        dto.setLossPowerFlag(test.isLossPowerFlag());
        dto.setInventNumber(test.getDevice().getInventNumber());
        dto.setStatus(test.getStatus());
        dto.setAlerted(test.isAlerted());

        if(test.getAuthors()==null) dto.setAuthors(Arrays.asList("DTN"));
        else dto.setAuthors(test.getAuthors().stream().map(a->a.name()).collect(Collectors.toList()));;
        return dto;
    }
}
