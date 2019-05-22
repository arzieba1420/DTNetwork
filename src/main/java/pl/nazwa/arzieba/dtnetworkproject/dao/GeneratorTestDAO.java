package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.GeneratorTest;

import java.util.List;

@Repository
public interface GeneratorTestDAO extends PagingAndSortingRepository<GeneratorTest,Integer> {

    Page<GeneratorTest> findByDevice_InventNumberOrderByDateDesc(String inventNumber, Pageable pageable);
    List<GeneratorTest> findAllByDevice_InventNumber(String inventNumber);
    Page<GeneratorTest> findAllByLossPowerFlag(boolean condition, Pageable pageable);
    GeneratorTest findTopByDevice_InventNumberOrderByDateDesc(String inv);



}
