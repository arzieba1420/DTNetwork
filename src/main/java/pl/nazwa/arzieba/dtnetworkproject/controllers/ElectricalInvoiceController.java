package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Under development - not active yet
 * Controller to manage electrical invoices for each building (model->BuildingType)
 */

@Controller
@RequestMapping("/invoices")
public class ElectricalInvoiceController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    private InvoiceServiceImpl invoiceService;

    //-----------------------------------------------------------------------CONSTRUCTOR----------------------------------------------------------------------------------------

    @Autowired
    public ElectricalInvoiceController(InvoiceServiceImpl invoiceService) {
        this.invoiceService = invoiceService;
    }

    //------------------------------------------------------------------------BUSINESS LOGIC-----------------------------------------------------------------------------------

    @GetMapping("/addForm/{building}")
    public String createInvoice(Model model, @PathVariable String building){
        return invoiceService.createInvoiceForm(model, building);
    }

    @PostMapping("/add")
    public String addInvoice( Model model, @Valid @ModelAttribute("newInvoice") InvoiceDTO dto, BindingResult bindingResult){
        return invoiceService.addInvoice(model, dto, bindingResult);
    }

    @GetMapping("/{building}/{page}")
    public String getAllForBuilding(Model model, @PathVariable String building, @PathVariable int page){
       return invoiceService.getAllForBuilding(model, building, page);
    }
}
