package pl.nazwa.arzieba.dtnetworkproject.dao;

import pl.nazwa.arzieba.dtnetworkproject.model.DeviceCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceCardDAO extends CrudRepository<DeviceCard,Integer> {

    List<DeviceCard> findAll();
    DeviceCard findByDeviceCardID(Integer integer);
    DeviceCard findByDevice_InventNumber(String inv);
    DeviceCard findBySignatureNumber(String sign);
}
