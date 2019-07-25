package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DamageNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.temporal.ChronoUnit;
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
    private PasswordEncoder passwordEncoder;
    private GeneratorTestDAO generatorTestDAO;



    @Autowired
    public MainController(UserDAO userDAO, IssueDocService issueDocService, DeviceDAO deviceDAO, ShortPostService postService, DeviceService deviceService, ShortPostDAO dao, PasswordEncoder passwordEncoder, GeneratorTestDAO generatorTestDAO) {
        this.postService = postService;
        this.deviceService = deviceService;
        this.dao = dao;
        this.deviceDAO=deviceDAO;
        this.issueDocService=issueDocService;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.generatorTestDAO = generatorTestDAO;
    }

    // required because login form redirects to localhost:8080/ not to /dtnetwork
    @GetMapping
    public String homeLog(Model model) {
        return home(model);
    }

    @GetMapping("/dtnetwork")
    public String home(Model model)  {

        Map<Integer, ShortPostDTO> mapa = new LinkedHashMap<>();
        List<Integer> keys = dao.findTop10ByOrderByDateDesc().stream().map(d -> d.getPostId()).collect(Collectors.toList());

        for (int i = 0; i < keys.size(); i++) {
            ShortPostDTO dto = postService.findById(keys.get(i));
            mapa.put(keys.get(i), dto);
        }
        List<String> rooms;
        rooms = ListOfEnumValues.rooms;

        List<Device> generators = deviceDAO.findAllByDeviceType(DeviceType.GENERATOR);

        Map<String, GeneratorTest> lastTests = new HashMap<>();

        for (Device generator: generators) {
            lastTests.put(generator.getInventNumber(),generatorTestDAO.findTopByDevice_InventNumberAndLossPowerFlagOrderByDateDesc(generator.getInventNumber(),false));
        }


        for (GeneratorTest test: lastTests.values()) {

            if (betweenDates(test.getDate(),new Date())==30&&test.isAlerted()==false){
                ShortPost post = new ShortPost();
                post.setDevice(test.getDevice());
                post.setPostDate(Calendar.getInstance());
                post.setDate(new Date());
                post.setContent("Wymagany test generatora! [SYSTEM]");
                post.setAuthor(Author.DTN);
                dao.save(post);
                test.setAlerted(true);
            }
        }

        String diary = userDAO.findByUsername(this.getUser()).getPersonalDiary();

        model.addAttribute("username",this.getUser());
        model.addAttribute("diary", diary);
        model.addAttribute("deviceServ", deviceService);
        model.addAttribute("lastPosts", mapa);
        model.addAttribute("rooms", rooms);
        model.addAttribute("generators", lastTests);
        model.addAttribute("today", Calendar.getInstance());

        return "index";
    }

    public static long betweenDates(Calendar firstDate, Date secondDate)
    {
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }

    @GetMapping("/dev/init")
        public String getInitData(){




        IntStream.range(10,25).forEach(i->{

            ShortPostDTO postDTO = new ShortPostDTO();
            postDTO.setInventNumber("S-001");
            postDTO.setContent(i-9+". Test content\nTest whitespace");
            postDTO.setAuthor(Author.Arek);
            postDTO.setDate("2012-01-"+i);

            postService.create(postDTO);

        });

        IntStream.range(1,9).forEach(i->{

            ShortPostDTO postDTO = new ShortPostDTO();
            postDTO.setInventNumber("S-002");
            postDTO.setContent(i+". Test content\nTest whitespace");
            postDTO.setAuthor(Author.Arek);
            postDTO.setDate("201"+i+"-02-01");

            postService.create(postDTO);

        });

        IntStream.range(1,17).forEach(i->{
            IssueDocumentDTO documentDTO = new IssueDocumentDTO();
            documentDTO.setIssueTittle(i+". Title "+i);
            documentDTO.setInventNumber("S-002");
            documentDTO.setDelivererName("Zeto");
            documentDTO.setDelivererNIP("NIP");
            documentDTO.setIssueSignature("ACK-DTN-"+i);
            documentDTO.setIssueDate("2020-02-"+i+10);
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

    @GetMapping("/dev/users")
    private @ResponseBody List<User> allUsers(){
        return userDAO.findAll();


    }

    @GetMapping("/dev/users/remove")
    private @ResponseBody void removeUsers(){
        userDAO.deleteAll();
    }

    @GetMapping("login")
    public String login(){

        return "login";
    }

    @GetMapping("/dev/users/setPass")
    public String setForgottenPass(Model model){
        List<String> users = userDAO.findAll().stream().map(d->d.getUsername()).collect(Collectors.toList());
        NewPassDTO newPassDTO = new NewPassDTO();
        newPassDTO.setOldPass("some old pass!");

        model.addAttribute("users",users);
        model.addAttribute("newPass",newPassDTO);
        return "users/setForgottenPassForm";
    }

    public String setForgottenPassErr(Model model, NewPassDTO newPassDTO){
        List<String> users = userDAO.findAll().stream().map(d->d.getUsername()).collect(Collectors.toList());

        model.addAttribute("users",users);
        model.addAttribute("newPass",newPassDTO);
        return "users/setForgottenPassForm";
    }

    @PostMapping("/dev/setPass")
    public String changePass(@Valid @ModelAttribute("newPass") NewPassDTO newPassDTO, BindingResult bindingResult, Model model){


        if(bindingResult.getFieldErrorCount()==1 && bindingResult.hasFieldErrors("oldPass") ){
            User user = userDAO.findByUsername(newPassDTO.getLogin());
            user.setPassword(passwordEncoder.encode(newPassDTO.getNewPass()));
            userDAO.save(user);
            return "redirect:/logout";
        }

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return setForgottenPassErr(model,newPassDTO);
        }

        if (!newPassDTO.getNewPass().equals(newPassDTO.getNewPassConfirmed())){
            List<FieldError> allErrors;
            FieldError fieldError = new FieldError("newPass","newPassConfirmed",newPassDTO.getNewPassConfirmed(),
                    false,null,null,"Hasła niezgodne!");

            bindingResult.addError(fieldError);
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return setForgottenPassErr(model,newPassDTO);
        }

        User user = userDAO.findByUsername(newPassDTO.getLogin());
        user.setPassword(passwordEncoder.encode(newPassDTO.getNewPass()));
        userDAO.save(user);
        return "redirect:/logout";
    }


    public String getUser(){
        String currentUserName = "Nieznany user";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        return currentUserName;
    }

    public Author string2Aut(String name){
        return Author.valueOf(name);
    }


}
