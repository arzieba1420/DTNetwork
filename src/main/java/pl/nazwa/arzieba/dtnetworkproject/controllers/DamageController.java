package pl.nazwa.arzieba.dtnetworkproject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/damages")
@Slf4j
public class DamageController {

    //-------------------------------------LOCAL VARIABLES--------------------------------------------------------------

    private DamageService damageService;

    //-------------------------------------CONSTRUCTOR------------------------------------------------------------------
    @Autowired
    public DamageController(DamageService damageService) {
        this.damageService = damageService;
    }

    //-------------------------------------BUSINESS LOGIC---------------------------------------------------------------

    @PostMapping("/addAsModel")
    public String add(@Valid @ModelAttribute("newDamage") DamageDTO damageDTO, BindingResult bindingResult,Model model){
        return damageService.addAsModel(damageDTO,bindingResult,model);
    }

    @GetMapping("/devices/{inventNumber}/{page}")
    public String getAllForDevice(@PathVariable String inventNumber, Model model, @PathVariable int page){
        return damageService.getAllForDevice(inventNumber,model,page);
    }

    @GetMapping("/addForm/{inventNumber}")
    public String createDamage(Model model,@PathVariable String inventNumber){
        return damageService.createDamage(model,inventNumber);
    }

    @GetMapping("/edit/{id}")
    public String editDamage(Model model, @PathVariable Integer id){
        return damageService.editDamage(model,id);
    }

    @PostMapping("/edit/{id}")
    public String saveEditedDamage(@Valid @ModelAttribute("newDamage") DamageDTO damageDTO, BindingResult bindingResult,@PathVariable Integer id,Model model){
        return damageService.saveEditedDamage(damageDTO,bindingResult,id,model);
    }
}
