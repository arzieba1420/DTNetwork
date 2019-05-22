package pl.nazwa.arzieba.dtnetworkproject.services.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.GeneratorTest;
import pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest.GeneratorTestMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private GeneratorTestDAO dao;
    private DeviceDAO deviceDAO;

    @Autowired
    public GeneratorServiceImpl(GeneratorTestDAO dao, DeviceDAO deviceDAO) {
        this.dao = dao;
        this.deviceDAO = deviceDAO;
    }

    @Override
    public GeneratorTestDTO create(GeneratorTestDTO dto) {

        GeneratorTest test = GeneratorTestMapper.map(dto,deviceDAO);
        dao.save(test);
        return dto;
    }

    @Override
    public List<GeneratorTestDTO> getAllTests(int page, int size, String inv) {

        Page<GeneratorTest> page1 = dao.findByDevice_InventNumberOrderByDateDesc(inv, PageRequest.of(
                page, size, Sort.Direction.DESC,"date"));

        return page1.stream()
                .map(GeneratorTestMapper::map)
                .collect(Collectors.toList());
    }
}
