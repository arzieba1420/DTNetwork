package pl.nazwa.arzieba.dtnetworkproject.services.invoice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.InvoiceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.InvoiceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.BuildingType;
import pl.nazwa.arzieba.dtnetworkproject.model.ElectricalInvoice;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import pl.nazwa.arzieba.dtnetworkproject.utils.invoices.InvoiceMapper;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InvoiceServiceImpl implements InvoiceService {

    //---------------------------------------------LOCAL VARIABLES------------------------------------------------------

    private InvoiceDAO invoiceDAO;
    @Value("${my.pagesize}")
    private int pagesize;

    //---------------------------------------------CONSTRUCTOR----------------------------------------------------------
    public InvoiceServiceImpl(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    //---------------------------------------------DAO methods----------------------------------------------------------

    @Override
    public InvoiceDTO create(InvoiceDTO dto) {
        invoiceDAO.save(InvoiceMapper.map(dto));
        return dto;
    }

    @Override
    public List<InvoiceDTO> findAll() {
        return invoiceDAO.findAll().stream().map(InvoiceMapper::map).collect(Collectors.toList());
    }

    @Override
    public Page<InvoiceDTO> getPage(int page, int size) {
        return null;
    }

    @Override
    public List<InvoiceDTO> getByYearInBuilding(int year, BuildingType buildingType) {
        Calendar start = new GregorianCalendar(year,0,1);
        Calendar end = new GregorianCalendar(year,11,31);
        return invoiceDAO.findAllByDateBetweenAndBuildingOrderByDateDesc(start,end,buildingType).stream().map(InvoiceMapper::map)
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
        return invoiceDAO.findAllByBuilding(PageRequest.of(page, size, Sort.Direction.DESC,"date"),buildingType).getContent();
    }


    //--------------------------------------------HTTP METHODS----------------------------------------------------------

    @Override
    public String createInvoiceForm(Model model, String building) {
        model.addAttribute("newInvoice", new InvoiceDTO());
        model.addAttribute("building", BuildingType.valueOf(building) );
        model.addAttribute("buildings", ListOfEnumValues.buildings);
        return "invoices/addInvoiceForm";
    }

    @Override
    public String addInvoice(Model model, InvoiceDTO dto, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return createInvoiceErr(model,dto,dto.getBuilding());
        }
        create(dto);
        return "redirect:/dtnetwork";
    }

    @Override
    public String getAllForBuilding(Model model, String building, int page) {
        List<ElectricalInvoice> invoices = getAllInBuilding(page-1,pagesize,BuildingType.valueOf(building));
        int numberOfPages = (invoiceDAO.findAllByBuilding(BuildingType.valueOf(building)).size())/pagesize + 1;
        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

        if(invoiceDAO.findAllByBuilding(BuildingType.valueOf(building)).size()%pagesize==0){
            numberOfPages--;
        }

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("invoices",invoices);
        model.addAttribute("amount", invoiceDAO.findAllByBuilding(BuildingType.valueOf(building)).size());
        model.addAttribute("building",building);
        model.addAttribute("monthsEng", Arrays.asList(CalendarUtil.monthsEng));
        model.addAttribute("monthsPol",Arrays.asList(CalendarUtil.monthsPol));
        return "invoices/allInvoices";
    }

//------------------------------Util methods----------------------------------------------------------------------------
    public String createInvoiceErr(Model model, InvoiceDTO dto, BuildingType building ){
        model.addAttribute("newInvoice", dto);
        model.addAttribute("building", building);
        return "invoices/addInvoiceForm";
    }
}
