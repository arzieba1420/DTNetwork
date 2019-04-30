package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.services.damage.DamageService;
import com.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import com.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/damages")
public class DamageController {



    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private DamageService damageService;
    private ShortPostService postService;


    @Autowired
    public DamageController(ShortPostService postService,DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, DamageService damageService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.damageService = damageService;
        this.postService = postService;
    }

    @GetMapping("/getAll")
    public List<DamageDTO> findAll(){
        return damageService.findAll();
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

    @GetMapping("/ids/{id}")
    public DamageDTO findById(@PathVariable Integer id){
        return damageService.findById(id);
    }

    @PutMapping("/update") //requires damageID
    public DamageDTO update( DamageDTO damageDTO){

       return damageService.update(damageDTO);
    }

    @DeleteMapping("/delete/{id}")
    public DamageDTO remove(@PathVariable Integer id){
        return damageService.remove(id);
    }

    @GetMapping("/authors/{author}")
    public List<DamageDTO> findByAuthor(@PathVariable Author author){
        return damageService.findByAuthor(author.name());
    }

    @GetMapping("/allBefore")
    public List<DamageDTO> findByDateBefore(String date){
        return  damageService.findByDateBefore(date);
    }

    @GetMapping("/allAfter")
    public List<DamageDTO> findByDateAfter(String date){
        return damageService.findByDateAfter(date);
    }

    @GetMapping("/devices/{inventNumber}")
    public List<DamageDTO> findByDeviceInventNumber(@PathVariable String inventNumber){
        return damageService.findByDeviceInventNumber(inventNumber);
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
