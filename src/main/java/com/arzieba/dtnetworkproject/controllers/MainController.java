package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.ShortPostDAO;
import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.services.device.DeviceService;
import com.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import com.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    private ShortPostService postService;
    private DeviceService deviceService;
    private ShortPostDAO dao;

    @Autowired
    public MainController(ShortPostService postService, DeviceService deviceService, ShortPostDAO dao) {
        this.postService = postService;
        this.deviceService = deviceService;
        this.dao = dao;
    }

    // required because login form redirects to localhost:8080/ not to /dtnetwork
    @GetMapping
    public String homeLog(Model model){
       return home(model);
    }

    @GetMapping("/dtnetwork")
    public String home(Model model){

        Map<Integer,ShortPostDTO> mapa = new LinkedHashMap<>();
        List<Integer> keys = dao.findTop10ByOrderByDateDesc().stream().map(d->d.getPostId()).collect(Collectors.toList());

        for (int i = 0; i <keys.size() ; i++) {
            ShortPostDTO dto = postService.findById(keys.get(i));
            mapa.put(keys.get(i),dto);
        }
        List<String> rooms;
        rooms = ListOfEnumValues.rooms;

        model.addAttribute("deviceServ", deviceService);
        model.addAttribute("lastPosts", mapa);
        model.addAttribute("rooms",rooms);

        return "index";
    }


    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request){
        return "redirect:"+request.getHeader("Referer");
    }


}
