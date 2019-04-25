package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.ShortPostDAO;
import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import com.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class ShortPostController {

    private ShortPostDAO postDAO;
    private DeviceDAO deviceDAO;
    private ShortPostService postService;

    @Autowired
    public ShortPostController(ShortPostDAO postDAO, DeviceDAO deviceDAO, ShortPostService postService) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
        this.postService = postService;

    }

    @GetMapping("/all")
    public List<ShortPostDTO> getAll(){
        return postService.findAll();
    }

    @GetMapping("/last5")
    public @ResponseBody List<ShortPostDTO> getLast5(){
        return postService.findLast5();
    }

    @GetMapping("/devices/{inv}")
    public List<ShortPostDTO> getAllForDevice(@PathVariable String inv){
        return postService.findByDevice(inv);
    }

    @GetMapping("/devices/last5/{inv}")
    public List<ShortPostDTO> getLast5ForDevice(@PathVariable String inv){
        return postService.find5ByDevice(inv);
    }

    @GetMapping("/authors/{author}")
    public List<ShortPostDTO> getAllForAuthor(@PathVariable Author author){
        return postService.findByAuthor(author);
    }

    @GetMapping("/{id}")
    public ShortPostDTO findById(@PathVariable Integer id){
        return postService.findById(id);
    }

    @PostMapping("/add")
    public String  create(Model model, ShortPostDTO dto){
        postService.create(dto);
        return "success";
    }

    @PostMapping("/addAsModel")
    public String  create2(Model model, @ModelAttribute("dto") ShortPostDTO dto){
        postService.create(dto);
        return "redirect:/dtnetwork";
    }

    @GetMapping("/delete/{id}")
    public String remove (@PathVariable Integer id, Model model){

         postService.remove(id);
         return "redirect:/dtnetwork";
    }

    @GetMapping("/addForm")
    public String addForm(Model model){
        model.addAttribute("newPost",new ShortPostDTO());
        model.addAttribute("authors", ListOfEnumValues.authors);
        Map<String,String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d->d.getInventNumber()).collect(Collectors.toList());
        for (String key:keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key,device.getDeviceDescription());
        }
        model.addAttribute("devices",mapa);
        return "posts/addPostForm";
    }


}
