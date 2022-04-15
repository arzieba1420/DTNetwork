package pl.nazwa.arzieba.dtnetworkproject.services.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.GeneratorTest;
import pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest.GeneratorTestMapper;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    private GeneratorTestDAO generatorTestDAO;
    private DeviceDAO deviceDAO;

    //------------------------------------------------------------CONSTRUCTOR------------------------------------------------------------------------------------------------

    @Autowired
    public GeneratorServiceImpl(GeneratorTestDAO generatorTestDAO, DeviceDAO deviceDAO) {
        this.generatorTestDAO = generatorTestDAO;
        this.deviceDAO = deviceDAO;
    }

    //------------------------------------------------------------CONTROLLER METHODS------------------------------------------------------------------------------------------

    @Override
    public GeneratorTestDTO create(GeneratorTestDTO dto) {
        dto.setAlerted(false);
        GeneratorTest test = GeneratorTestMapper.map(dto,deviceDAO);
        test.setAuthorsCommaSeparated(String.join(",",dto.getAuthors()));
        generatorTestDAO.save(test);
        return dto;
    }
}
