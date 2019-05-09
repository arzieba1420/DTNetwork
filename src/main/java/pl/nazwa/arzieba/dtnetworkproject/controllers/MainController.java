package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ShortPostService postService;
    private DeviceService deviceService;
    private ShortPostDAO dao;
    public static Authentication authentication;

    @Autowired
    public MainController( ShortPostService postService, DeviceService deviceService, ShortPostDAO dao) {
        this.postService = postService;
        this.deviceService = deviceService;
        this.dao = dao;
    }

    // required because login form redirects to localhost:8080/ not to /dtnetwork
    @GetMapping
    public String homeLog(Model model, Authentication authentication) {
        return home(model,authentication);
    }

    @GetMapping("/dtnetwork")
    public String home(Model model, Authentication authentication) {

        Map<Integer, ShortPostDTO> mapa = new LinkedHashMap<>();
        List<Integer> keys = dao.findTop10ByOrderByDateDesc().stream().map(d -> d.getPostId()).collect(Collectors.toList());

        for (int i = 0; i < keys.size(); i++) {
            ShortPostDTO dto = postService.findById(keys.get(i));
            mapa.put(keys.get(i), dto);
        }
        List<String> rooms;
        rooms = ListOfEnumValues.rooms;

        this.authentication = authentication;


        /*UserDetails userDetails = new  InMemoryUserDetailsManager().loadUserByUsername(authentication.getName());

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();*/

        model.addAttribute("deviceServ", deviceService);
        model.addAttribute("lastPosts", mapa);
        model.addAttribute("rooms", rooms);

        logger.info("User has been logged in: " + authentication.getName() );
        return "index";
    }


    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request) {
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/dev/logs")
    private String open( HttpServletRequest request ) {

        Path sourcePath = Paths.get(System.getProperty("java.io.tmpdir")+"/logs.txt");
        Path targetPath = Paths.get(System.getProperty("java.io.tmpdir")+"/logs2.txt");

        try {
            Path path = Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);//copy with REPLACE_EXISTING option

        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(System.getProperty("java.io.tmpdir")+"/logs2.txt");

        try {
            final String cmd =
                    String.format( "cmd.exe /C start %s", file.getAbsolutePath());
            Runtime.getRuntime().exec( cmd );
        }
        catch( final Throwable t ) {
            t.printStackTrace();
        }
       return  "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/dev/exc")
    public String exc(){
        try {
            throw new RuntimeException();
        } catch (RuntimeException rexc){
            logger.error("Runtime exc thrown by ADMIN");
            return "/error";
        }

    }

    @GetMapping("/dev")
    public String dev(){
        return "dev/index";
    }

    @GetMapping("/inBuild")
    public String inBuild(){
        return "build";
    }
}
