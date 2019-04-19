package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.services.device.DeviceService;
import com.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dtnetwork")
public class MainController {

    private ShortPostService postService;
    private DeviceService deviceService;

    @Autowired
    public MainController(ShortPostService postService, DeviceService deviceService) {
        this.postService = postService;
        this.deviceService = deviceService;
    }

    @GetMapping
    public String home(Model model){
        List<ShortPostDTO> list = postService.findLast5();
        for (ShortPostDTO dto:list) {
            String newDesc = deviceService.findByInventNumber(dto.getInventNumber()).getDeviceDescription()+" "+
                    deviceService.findByInventNumber(dto.getInventNumber()).getRoom();
            dto.setInventNumber(newDesc);
        }

        model.addAttribute("deviceServ", deviceService);
        model.addAttribute("lastPosts", list);
        return "index";
    }

}
