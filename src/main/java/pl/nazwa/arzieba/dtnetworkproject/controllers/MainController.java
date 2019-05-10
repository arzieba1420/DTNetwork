package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

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
public class MainController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ShortPostService postService;
    private DeviceService deviceService;
    private ShortPostDAO dao;
    private DeviceDAO deviceDAO;
    private IssueDocService issueDocService;
    public static Authentication authentication;

    @Autowired
    public MainController(IssueDocService issueDocService, DeviceDAO deviceDAO, ShortPostService postService, DeviceService deviceService, ShortPostDAO dao) {
        this.postService = postService;
        this.deviceService = deviceService;
        this.dao = dao;
        this.deviceDAO=deviceDAO;
        this.issueDocService=issueDocService;
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
            throw new RuntimeException();
        } catch (RuntimeException rexc){
            logger.error("Runtime exc thrown by ADMIN");
            return "/error";
        }

    }

    @GetMapping("/login2")
    public String loginPage(){
        return "login2";
    }

    @GetMapping("/dev")
    public String dev(){
        return "dev/index";
    }

    @GetMapping("/inBuild")
    public String inBuild(){
        return "build";
    }

 /*   @PostMapping("/perform_login")
    public String perform(@ModelAttribute("user") UserDetails userDetails ){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.setAuthenticationManager(a);
    }*/
}
