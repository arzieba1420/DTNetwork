package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.DeviceCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceCardDAO extends CrudRepository<DeviceCard,Integer> {
}
