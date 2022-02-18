package pl.nazwa.arzieba.dtnetworkproject.services.generator;


import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;

import java.util.List;

public interface GeneratorService {

    GeneratorTestDTO create(GeneratorTestDTO dto);
    List<GeneratorTestDTO> getAllTests(int page, int size, String inv);
}
