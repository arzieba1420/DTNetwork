package pl.nazwa.arzieba.dtnetworkproject.services.invoice;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.InvoiceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.BuildingType;
import pl.nazwa.arzieba.dtnetworkproject.model.ElectricalInvoice;
import java.util.List;

@Service
public interface InvoiceService {

    InvoiceDTO create(InvoiceDTO dto);
    List<InvoiceDTO> findAll();
    Page<InvoiceDTO> getPage(int page, int size);
    List<InvoiceDTO> getByYearInBuilding(int year, BuildingType buildingType);
    double sumInYearInBuilding(int year, BuildingType buildingType);
    List<ElectricalInvoice> getAllInBuilding(int page, int size, BuildingType buildingType);
    String createInvoiceForm(Model model,String building);
    String addInvoice(Model model,InvoiceDTO dto, BindingResult bindingResult);
    String getAllForBuilding(Model model,String building,int page);
}
