package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Damage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface DamageDAO extends CrudRepository<Damage,Integer> {
    List<Damage> findAll();
    Damage findByDevice_InventNumber(String inventNumber);
    Optional<Damage> findById(Integer id);
    void deleteDamageByDevice_InventNumber(String inventNumber);
    List<Damage> findByAuthor(String author);
    List<Damage> findByDamageDateAfter(Calendar date);
    List<Damage> findByDamageDateBefore(Calendar date);
}
