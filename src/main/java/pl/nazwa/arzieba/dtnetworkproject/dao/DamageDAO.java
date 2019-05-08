package pl.nazwa.arzieba.dtnetworkproject.dao;

import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface DamageDAO extends CrudRepository<Damage,Integer> {
    List<Damage> findAll();
    List<Damage> findByDevice_InventNumberOrderByDamageDateDesc(String inventNumber);
    Optional<Damage> findById(Integer id);
    List<Damage> findByAuthor(Author author);
    List<Damage> findByDamageDateAfter(Calendar date);
    List<Damage> findByDamageDateBefore(Calendar date);
}
