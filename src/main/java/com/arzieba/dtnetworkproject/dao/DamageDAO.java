package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Damage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageDAO extends CrudRepository<Damage,Integer> {
}
