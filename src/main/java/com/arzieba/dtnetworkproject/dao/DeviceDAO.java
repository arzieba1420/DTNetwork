package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDAO extends CrudRepository<Device,String> {
}
