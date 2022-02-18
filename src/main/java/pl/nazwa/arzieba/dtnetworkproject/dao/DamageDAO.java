package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface DamageDAO extends PagingAndSortingRepository<Damage,Integer> {

    Damage findByDamageId(Integer id);
    List<Damage> findAll();
    List<Damage> findByDevice_InventNumber(String inventNumber);
    Page<Damage> findByDevice_InventNumber(Pageable pageable, String inventNumber);
    Optional<Damage> findById(Integer id);
    List<Damage> findByAuthor(Author author);
    List<Damage> findByDamageDateAfter(Calendar date);
    List<Damage> findByDamageDateBefore(Calendar date);
}
