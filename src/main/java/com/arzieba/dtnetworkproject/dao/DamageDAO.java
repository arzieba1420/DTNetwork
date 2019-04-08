package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamageDAO extends CrudRepository<Damage,Integer> {
    List<Damage> findAll();

    List<Damage> findByDevice(Device device);
}
