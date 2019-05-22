package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.BuildingType;
import pl.nazwa.arzieba.dtnetworkproject.model.ElectricalInvoice;

import java.util.Calendar;
import java.util.List;

@Repository
public interface InvoiceDAO extends PagingAndSortingRepository<ElectricalInvoice,String> {

    List<ElectricalInvoice> findAll();
    Page<ElectricalInvoice> findAll(Pageable pageable);
    Page<ElectricalInvoice> findAllByBuilding(Pageable pageable, BuildingType buildingType);
    ElectricalInvoice findByInvoiceId(String id);
    List<ElectricalInvoice> findAllByDateBetweenAndBuildingOrderByDateDesc(Calendar start, Calendar end, BuildingType buildingType);

}
