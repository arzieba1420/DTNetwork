package pl.nazwa.arzieba.dtnetworkproject.services.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.GeneratorTest;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest.GeneratorTestMapper;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private GeneratorTestDAO generatorTestDAO;
    private DeviceDAO deviceDAO;

    @Autowired
    public GeneratorServiceImpl(GeneratorTestDAO generatorTestDAO, DeviceDAO deviceDAO) {
        this.generatorTestDAO = generatorTestDAO;
        this.deviceDAO = deviceDAO;
    }

    @Override
    public GeneratorTestDTO create(GeneratorTestDTO dto) {
        dto.setAlerted(false);
        GeneratorTest test = GeneratorTestMapper.map(dto,deviceDAO);
        test.setAuthorsCommaSeparated(String.join(",",dto.getAuthors()));
        generatorTestDAO.save(test);
        return dto;
    }
}
