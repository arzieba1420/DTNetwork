package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.generator.GeneratorService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("generators")
public class GeneratorController {

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DeviceService deviceService;
    private GeneratorTestDAO generatorTestDAO;
    private GeneratorService generatorService;
    private MainController mainController;
    private ShortPostService postService;
    private DamageController damageController;
    @Value("${my.pagesize}")
    int pagesize;

    public GeneratorController(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, DeviceService deviceService, GeneratorTestDAO generatorTestDAO, GeneratorService generatorService, MainController mainController, ShortPostService postService, DamageController damageController) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.deviceService = deviceService;
        this.generatorTestDAO = generatorTestDAO;
        this.generatorService = generatorService;
        this.mainController = mainController;
        this.postService = postService;
        this.damageController = damageController;
    }

    @GetMapping("/{inv}/{page}")
    public String getAllTests(@PathVariable String inv, Model model, @PathVariable int page){

        List<GeneratorTestDTO> testPage = generatorService.getAllTests(page-1,pagesize,inv);
        DeviceDTO dto = deviceService.findByInventNumber(inv);
        int numberOfPages = (generatorTestDAO.findAllByDevice_InventNumber(inv).size())/pagesize+1;
        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

        if(generatorTestDAO.findAllByDevice_InventNumber(inv).size()%pagesize==0){
            numberOfPages --;
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
        model.addAttribute("tests",testPage);
        model.addAttribute("amount", generatorTestDAO.findAllByDevice_InventNumber(inv).size());
        model.addAttribute("dto", dto);

        return "devices/getAllTests";
    }
}
