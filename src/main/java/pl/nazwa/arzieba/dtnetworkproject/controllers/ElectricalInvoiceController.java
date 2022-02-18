package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.dao.InvoiceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.InvoiceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.BuildingType;
import pl.nazwa.arzieba.dtnetworkproject.model.ElectricalInvoice;
import pl.nazwa.arzieba.dtnetworkproject.services.invoice.InvoiceServiceImpl;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class ElectricalInvoiceController {

    private InvoiceServiceImpl invoiceService;
    private InvoiceDAO invoiceDAO;
    @Value("${my.pagesize}")
    private int pagesize;

    public ElectricalInvoiceController(InvoiceServiceImpl invoiceService, InvoiceDAO invoiceDAO) {
        this.invoiceService = invoiceService;
        this.invoiceDAO = invoiceDAO;
    }

    @GetMapping("/addForm/{building}")
    public String createInvoice(Model model, @PathVariable String building){

        model.addAttribute("newInvoice", new InvoiceDTO());
        model.addAttribute("building", BuildingType.valueOf(building) );
        model.addAttribute("buildings", ListOfEnumValues.buildings);

        return "invoices/addInvoiceForm";
    }

    public String createInvoiceErr(Model model, InvoiceDTO dto, BuildingType building ){

        model.addAttribute("newInvoice", dto);
        model.addAttribute("building", building);

        return "invoices/addInvoiceForm";
    }

    @PostMapping("/add")
    public String addInvoice( Model model, @Valid @ModelAttribute("newInvoice") InvoiceDTO dto, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());

            return createInvoiceErr(model,dto,dto.getBuilding());
        }

        invoiceService.create(dto);

        return "redirect:/dtnetwork";
    }

    @GetMapping("/{building}/{page}")
    public String getAllForBuilding(Model model, @PathVariable String building, @PathVariable int page){

        List<ElectricalInvoice> invoices = invoiceService.getAllInBuilding(page-1,pagesize,BuildingType.valueOf(building));
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
}
