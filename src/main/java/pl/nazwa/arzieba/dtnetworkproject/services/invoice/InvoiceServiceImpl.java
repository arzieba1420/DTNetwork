package pl.nazwa.arzieba.dtnetworkproject.services.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.InvoiceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.InvoiceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.BuildingType;
import pl.nazwa.arzieba.dtnetworkproject.model.ElectricalInvoice;
import pl.nazwa.arzieba.dtnetworkproject.utils.invoices.InvoiceMapper;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceDAO dao;
    public InvoiceServiceImpl(InvoiceDAO dao) {
        this.dao = dao;
    }

    @Override
    public InvoiceDTO create(InvoiceDTO dto) {

        dao.save(InvoiceMapper.map(dto));

        return dto;
    }

    @Override
    public List<InvoiceDTO> findAll() {

        return dao.findAll().stream().map(InvoiceMapper::map).collect(Collectors.toList());
    }

    @Override
    public Page<InvoiceDTO> getPage(int page, int size) {
        return null;
    }

    @Override
    public List<InvoiceDTO> getByYearInBuilding(int year, BuildingType buildingType) {

        Calendar start = new GregorianCalendar(year,0,1);
        Calendar end = new GregorianCalendar(year,11,31);

        return dao.findAllByDateBetweenAndBuildingOrderByDateDesc(start,end,buildingType).stream().map(InvoiceMapper::map)
                     .collect(Collectors.toList());
    }

    @Override
    public double sumInYearInBuilding(int year, BuildingType buildingType) {

        List<ElectricalInvoice> invoices = getByYearInBuilding(year,buildingType).stream().map(InvoiceMapper::map).collect(Collectors.toList());
        double sum=0;

        for (ElectricalInvoice invoice: invoices) {
            sum+=invoice.getGrossValue();
        }

        return sum;
    }

    @Override
    public List<ElectricalInvoice> getAllInBuilding(int page, int size, BuildingType buildingType) {

        return dao.findAllByBuilding(PageRequest.of(page, size, Sort.Direction.DESC,"date"),buildingType).getContent();

    }
}
