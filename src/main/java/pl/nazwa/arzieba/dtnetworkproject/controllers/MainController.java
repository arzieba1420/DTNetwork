package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.User;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DamageNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class MainController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH = "/error";

    private ShortPostService postService;
    private DeviceService deviceService;
    private ShortPostDAO dao;
    private DeviceDAO deviceDAO;
    private IssueDocService issueDocService;
    private UserDAO userDAO;



    @Autowired
    public MainController( UserDAO userDAO, IssueDocService issueDocService, DeviceDAO deviceDAO, ShortPostService postService, DeviceService deviceService, ShortPostDAO dao) {
        this.postService = postService;
        this.deviceService = deviceService;
        this.dao = dao;
        this.deviceDAO=deviceDAO;
        this.issueDocService=issueDocService;
        this.userDAO = userDAO;
    }

    // required because login form redirects to localhost:8080/ not to /dtnetwork
    @GetMapping
    public String homeLog(Model model) {
        return home(model);
    }

    @GetMapping("/dtnetwork")
    public String home(Model model) {

        Map<Integer, ShortPostDTO> mapa = new LinkedHashMap<>();
        List<Integer> keys = dao.findTop10ByOrderByDateDesc().stream().map(d -> d.getPostId()).collect(Collectors.toList());

        for (int i = 0; i < keys.size(); i++) {
            ShortPostDTO dto = postService.findById(keys.get(i));
            mapa.put(keys.get(i), dto);
        }
        List<String> rooms;
        rooms = ListOfEnumValues.rooms;




        /*UserDetails userDetails = new  InMemoryUserDetailsManager().loadUserByUsername(authentication.getName());

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();*/




        model.addAttribute("deviceServ", deviceService);
        model.addAttribute("lastPosts", mapa);
        model.addAttribute("rooms", rooms);


        return "index";
    }

    @GetMapping("/dev/init")
        public String getInitData(){


        IntStream.range(10,25).forEach(i->{

            ShortPostDTO postDTO = new ShortPostDTO();
            postDTO.setInventNumber("S-001");
            postDTO.setContent(i-9+". Test content\nTest whitespace");
            postDTO.setAuthor(Author.Arek);
            postDTO.setDate(i+"-01-2012");

            postService.create(postDTO);

        });

        IntStream.range(1,9).forEach(i->{

            ShortPostDTO postDTO = new ShortPostDTO();
            postDTO.setInventNumber("S-002");
            postDTO.setContent(i+". Test content\nTest whitespace");
            postDTO.setAuthor(Author.Arek);
            postDTO.setDate("02-01-201"+i);

            postService.create(postDTO);

        });

        IntStream.range(1,17).forEach(i->{
            IssueDocumentDTO documentDTO = new IssueDocumentDTO();
            documentDTO.setIssueTittle(i+". Title "+i);
            documentDTO.setInventNumber("S-002");
            documentDTO.setDelivererName("Zeto");
            documentDTO.setDelivererNIP("NIP");
            documentDTO.setIssueSignature("ACK-DTN-"+i);
            documentDTO.setIssueDate(i+10+"-02-2020");
            documentDTO.setIssueDetails("Some text\nSome text 2");

            issueDocService.create(documentDTO);


        });


            return "redirect:/";
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
            throw new DamageNotFoundException();
        } catch (DamageNotFoundException rexc){
            logger.error("Runtime exc thrown by ADMIN");
            return error();
        }

    }

    @GetMapping("/login2")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/dev")
    public String dev(){
        return "dev/index";
    }

    @GetMapping("/inBuild")
    public String inBuild(){
        return "build";
    }

    @GetMapping(value = PATH)
    public String error(){
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @GetMapping("/users")
    private @ResponseBody List<User> allUsers(){
        return userDAO.findAll();


    }

    @GetMapping("/users/remove")
    private @ResponseBody void removeUsers(){
        userDAO.deleteAll();
    }

    @GetMapping("login")
    public String login(){

        return "login";
    }

    public String getUser(){
        String currentUserName = "NOT KNOWN";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }

        return currentUserName;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
