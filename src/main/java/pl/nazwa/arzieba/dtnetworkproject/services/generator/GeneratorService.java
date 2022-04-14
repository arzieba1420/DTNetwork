package pl.nazwa.arzieba.dtnetworkproject.services.generator;


import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;

import java.util.List;

public interface GeneratorService {

    GeneratorTestDTO create(GeneratorTestDTO dto);

}
