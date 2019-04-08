package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDAO extends CrudRepository<Device,String> {

    List<Device> findAll();

}
