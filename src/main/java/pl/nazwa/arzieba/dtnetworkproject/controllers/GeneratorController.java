package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;

@Controller
@RequestMapping("generators")
public class GeneratorController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    private DeviceService deviceService;

    //----------------------------------------------------------------------CONSTRUCTOR-------------------------------------------------------------------------------------------

    @Autowired
    public GeneratorController( DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    //---------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    @GetMapping("/{inv}/{page}")
    public String getAllTests(@PathVariable String inv, Model model, @PathVariable int page){
        return deviceService.getAllGeneratorTests(inv, model, page);
    }
}
