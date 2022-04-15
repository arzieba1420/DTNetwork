package pl.nazwa.arzieba.dtnetworkproject.controllers;


import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.*;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/devices")
public class DeviceController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    private DeviceService deviceService;
    private DamageService damageService;

    //--------------------------------------------------------------------CONSTRUCTOR------------------------------------------------------------------------------------------

    @Autowired
    public DeviceController(DeviceService deviceService, DamageService damageService) {
        this.deviceService = deviceService;
        this.damageService = damageService;
    }

    //--------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    //Returns DeviceDTO by ID = inventNumber
    @GetMapping("/{inventNumber}")
    public String generateMainViewForDevice(@PathVariable String inventNumber, Model model){
        return deviceService.generateMainViewForDevice(inventNumber,model);
    }

    @PostMapping("/addAsModel")
    public String createDevice(@Valid @ModelAttribute("newDevice") DeviceDTO dto, BindingResult bindingResult, Model model){
        return deviceService.createDevice(dto,bindingResult,model);
    }

    @GetMapping("/addForm/{room}")
    public String addForm(Model model, @PathVariable String room){
        return deviceService.createDeviceInRoom(model,room);
    }

    @GetMapping("/home")
    public String home(){
        return "redirect:/dtnetwork";
    }

    @GetMapping("/activityForm/{inventNumber}")
    public String createActivityFormForGenerator(Model model, @PathVariable String inventNumber){
        return deviceService.createActivityForGenerator(model, inventNumber);
    }

    @PostMapping("/addTest")
    public String addGeneratorActivity(@Valid @ModelAttribute("newTest") GeneratorTestDTO testDTO, BindingResult bindingResult, Model model ){
        return damageService.addGeneratorActivity(testDTO, bindingResult, model);
    }

    @PostMapping("/chillerSet/{inventNumber}")
    public String setChillerTemp( @ModelAttribute("chillerSetDTO") @Valid ChillerSetDTO chillerSetDTO, BindingResult binding, Model model, @PathVariable String inventNumber){
        return deviceService.setChillerTemp(chillerSetDTO, binding, model, inventNumber);
        }

    @PostMapping("/drycoolerSet/{inventNumber}")
    public String setDrycoolerTemp( @ModelAttribute("drycoolerSetDTO") @Valid DrycoolerSetDTO drycoolerSetDTO, BindingResult binding, Model model, @PathVariable String inventNumber){
        return deviceService.setDrycoolerTemp(drycoolerSetDTO, binding, model, inventNumber);
    }

    @GetMapping("/setUnactive/{inv}")
    public String changeRoom (Model model, @PathVariable String inv){
        deviceService.changeRoom(inv);
        return generateMainViewForDevice(inv,model);
    }
}
