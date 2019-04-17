package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.ShortPostDAO;
import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public List<ShortPostDTO> getLast5(){
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
    public ShortPostDTO create(@RequestBody ShortPostDTO dto){
        return postService.create(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ShortPostDTO remove (@PathVariable Integer id){
        return postService.remove(id);
    }


}
