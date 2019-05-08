package pl.nazwa.arzieba.dtnetworkproject.controllers;

import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import pl.nazwa.arzieba.dtnetworkproject.services.damage.DamageService;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/damages")
public class DamageController {



    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private ShortPostService postService;
    private DeviceService deviceService;


    @Autowired
    public DamageController(DeviceService deviceService, ShortPostService postService,DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, DamageService damageService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.damageService = damageService;
        this.postService = postService;
        this.deviceService=deviceService;
    }



    @PostMapping("/addAsModel")
    public String add(@ModelAttribute("dto") DamageDTO damageDTO){

        if(!damageDTO.isNewPostFlag()) {
            damageService.create(damageDTO);

        } else{
            ShortPostDTO dto = new ShortPostDTO();
            dto.setDate(damageDTO.getDamageDate());
            dto.setAuthor(damageDTO.getAuthor());
            dto.setInventNumber(damageDTO.getDeviceInventNumber());
            dto.setContent("New damage! Click target for more...");
            dto.setForDamage(true);
            damageService.create(damageDTO);
            postService.create(dto);

        }
        return "redirect:/dtnetwork";
    }

    @GetMapping("/devices/{inventNumber}")
    public String getAllForDevice(@PathVariable String inventNumber, Model model){
        List<Damage> damages = damageDAO.findByDevice_InventNumberOrderByDamageDateDesc(inventNumber);
        DeviceDTO dto = deviceService.findByInventNumber(inventNumber);
        Map<Integer,DamageDTO> mapa = new LinkedHashMap<>();

        for (Damage damage: damages) {
            mapa.put(damage.getDamageId(), DamageMapper.map(damage));
        }

        model.addAttribute("damages",mapa);
        model.addAttribute("dto",dto);

        return "damages/allDamagesForDevice";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String createDamage(Model model,@PathVariable String inventNumber){

        model.addAttribute("newDamage", new DamageDTO());
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber",inventNumber);
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);

        return "damages/addForm";
    }

}
