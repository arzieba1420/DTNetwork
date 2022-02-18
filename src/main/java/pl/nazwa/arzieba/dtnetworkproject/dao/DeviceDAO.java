package pl.nazwa.arzieba.dtnetworkproject.dao;

import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.DeviceType;

import java.util.List;

@Repository
public interface DeviceDAO extends CrudRepository<Device,String> {

    List<Device> findAll();
    List<Device> findAllByDeviceType(DeviceType deviceType);
    Device findByInventNumber(String string);
    void deleteDeviceByInventNumber(String inventNumber);
}
