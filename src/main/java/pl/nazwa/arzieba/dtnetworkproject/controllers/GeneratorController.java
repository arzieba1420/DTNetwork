package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.generator.GeneratorService;

@Controller
@RequestMapping("generators")
public class GeneratorController {

    private GeneratorService generatorService;
    private DeviceService deviceService;

    public GeneratorController(GeneratorService generatorService, DeviceService deviceService) {
        this.generatorService = generatorService;
        this.deviceService = deviceService;
    }

    @GetMapping("/{inv}/{page}")
    public String getAllTests(@PathVariable String inv, Model model, @PathVariable int page){
        return deviceService.getAllGeneratorTests(inv, model, page);
    }
}
