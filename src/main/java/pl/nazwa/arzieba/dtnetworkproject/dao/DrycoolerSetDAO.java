package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.DrycoolerSet;

@Repository
public interface DrycoolerSetDAO extends CrudRepository<DrycoolerSet, Integer> {
}
