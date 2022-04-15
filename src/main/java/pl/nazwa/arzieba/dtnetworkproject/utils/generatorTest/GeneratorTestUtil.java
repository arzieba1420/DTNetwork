package pl.nazwa.arzieba.dtnetworkproject.utils.generatorTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;

@Component
public class GeneratorTestUtil {

    GeneratorTestDAO dao;

    @Autowired
    public GeneratorTestUtil(GeneratorTestDAO dao) {
        this.dao=dao;
    }
}
