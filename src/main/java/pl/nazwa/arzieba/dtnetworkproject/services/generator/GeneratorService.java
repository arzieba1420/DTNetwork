package pl.nazwa.arzieba.dtnetworkproject.services.generator;



import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;

@Service
public interface GeneratorService {

    GeneratorTestDTO create(GeneratorTestDTO dto);
}
