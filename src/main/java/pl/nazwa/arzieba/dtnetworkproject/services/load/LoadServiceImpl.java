package pl.nazwa.arzieba.dtnetworkproject.services.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.DtNetworkApplication;
import pl.nazwa.arzieba.dtnetworkproject.configuration.MyPropertiesConfig;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.LeaveApplyPreparer;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import pl.nazwa.arzieba.dtnetworkproject.utils.mail.EmailConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoadServiceImpl implements LoadService {

    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ERROR_PATH = "/error";
    public static ApplicationHome applicationHome = new ApplicationHome(DtNetworkApplication.class);
    private ShortPostService postService;
    private DeviceService deviceService;
    private ShortPostDAO shortPostDAO;
    private DeviceDAO deviceDAO;
    private IssueDocService issueDocService;
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private GeneratorTestDAO generatorTestDAO;
    private MyPropertiesConfig propertiesConfig;
    private ApplicationArguments applicationArguments;

    private EmailConfiguration emailConfiguration;

    //------------------------------------------------------------CONSTRUCTOR---------------------------------------------------------------------------------------------

    @Autowired
    public LoadServiceImpl(ShortPostService postService, DeviceService deviceService, ShortPostDAO shortPostDAO, DeviceDAO deviceDAO, IssueDocService issueDocService, UserDAO userDAO, PasswordEncoder passwordEncoder, GeneratorTestDAO generatorTestDAO, MyPropertiesConfig propertiesConfig, ApplicationArguments applicationArguments, EmailConfiguration emailConfiguration) {
        this.postService = postService;
        this.deviceService = deviceService;
        this.shortPostDAO = shortPostDAO;
        this.deviceDAO = deviceDAO;
        this.issueDocService = issueDocService;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.generatorTestDAO = generatorTestDAO;
        this.propertiesConfig = propertiesConfig;
        this.applicationArguments = applicationArguments;
        this.emailConfiguration = emailConfiguration;
    }

    //------------------------------------------------------------CONTROLLER METHODS---------------------------------------------------------------------------------------------

    @Override
    public String loadHome(Model model, Principal principal) {
        List<String> rooms;
        rooms = ListOfEnumValues.rooms;
        List<Device> generators = deviceDAO.findAllByDeviceType(DeviceType.GENERATOR);
        Map<String, GeneratorTest> lastTests = new HashMap<>();
        String diary = userDAO.findByUsername(this.getUser()).getPersonalDiary();


        User user = userDAO.findByUsername(principal.getName());
        boolean credentialsPresent = user.isActiveACK();



        for (Device generator: generators) {
            lastTests.put(generator.getInventNumber(),generatorTestDAO.findTopByDevice_InventNumberAndLossPowerFlagOrderByDateDesc(generator.getInventNumber(),false));
        }

        if(!lastTests.values().contains(null)) {

            for (GeneratorTest test : lastTests.values()) {

                if (betweenDates(test.getDate(), new Date()) >= 30 && !test.isAlerted()) {
                    ShortPost post = new ShortPost();
                    post.setDevice(test.getDevice());
                    post.setPostDate(Calendar.getInstance());
                    post.setDate(new Date());
                    post.setContent("Wymagany test generatora! [SYSTEM]");
                    post.setAuthor(Author.DTN);
                    post.setPostLevel(PostLevel.INFO);
                    shortPostDAO.save(post);
                    test.setAlerted(true);
                }
            }
        }

        Map<Integer, ShortPostDTO> mapa = new LinkedHashMap<>();
        List<Integer> keys = shortPostDAO.findTop10ByOrderByPostIdDesc().stream().map(d -> d.getPostId()).collect(Collectors.toList());

        for (int i = 0; i < keys.size(); i++) {
            ShortPostDTO dto = postService.findById(keys.get(i));
            mapa.put(keys.get(i), dto);
        }

        model.addAttribute("username",this.getUser());
        model.addAttribute("diary", diary);
        model.addAttribute("calendarEntry", userDAO.findByUsername("DTN").getPersonalDiary() );
        model.addAttribute("deviceServ", deviceService);
        model.addAttribute("lastPosts", mapa);
        model.addAttribute("rooms", rooms);
        model.addAttribute("generators", lastTests);
        model.addAttribute("today", Calendar.getInstance());
        model.addAttribute("ackCredentials", credentialsPresent);
        return "index";
    }

    @Override
    public String openLogFile(HttpServletRequest request) {
        Path sourcePath = Paths.get(System.getProperty("java.io.tmpdir")+"/logs.txt");
        Path targetPath = Paths.get(System.getProperty("java.io.tmpdir")+"/logs2.txt");
        File file = new File(System.getProperty("java.io.tmpdir")+"/logs2.txt");
        logger.info("Absolute path to logs .txt file:"+file.getAbsolutePath());

        try {
            Path path = Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);//copy with REPLACE_EXISTING option
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final String cmd = String.format( "cmd.exe /C start %s", file.getAbsolutePath());
            Runtime.getRuntime().exec( cmd );
        }
        catch( final Throwable t ) {
            t.printStackTrace();
        }
        return  "redirect:" + request.getHeader("Referer");
    }

    @Override
    public String setForgottenPass(Model model) {
        List<String> users = userDAO.findAll().stream().map(d->d.getUsername()).collect(Collectors.toList());
        NewPassDTO newPassDTO = new NewPassDTO();
        newPassDTO.setOldPass("some old pass!");
        model.addAttribute("users",users);
        model.addAttribute("newPass",newPassDTO);
        return "users/setForgottenPassForm";
    }

    @Override
    public String changePass(NewPassDTO newPassDTO, BindingResult bindingResult, Model model) {
        User user = userDAO.findByUsername(newPassDTO.getLogin());

        if(bindingResult.getFieldErrorCount()==1 && bindingResult.hasFieldErrors("oldPass") ){            ;
            user.setPassword(passwordEncoder.encode(newPassDTO.getNewPass()));
            userDAO.save(user);
            return "redirect:/logout";
        }

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return setForgottenPassErr(model,newPassDTO);
        }

        if (!newPassDTO.getNewPass().equals(newPassDTO.getNewPassConfirmed())){
            List<FieldError> allErrors;
            FieldError fieldError = new FieldError("newPass","newPassConfirmed",newPassDTO.getNewPassConfirmed(),
                    false,null,null,"Hasła niezgodne!");
            allErrors = bindingResult.getFieldErrors();
            bindingResult.addError(fieldError);
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return setForgottenPassErr(model,newPassDTO);
        }

        user.setPassword(passwordEncoder.encode(newPassDTO.getNewPass()));
        userDAO.save(user);
        return "redirect:/logout";
    }

    @Override
    public String prepareSingleApplyMail(Model model, Principal principal) {
        LeaveApplyPreparer applyPreparer = new LeaveApplyPreparer();
        applyPreparer.setSign(userDAO.findByUsername(principal.getName()).getSigning());
        applyPreparer.setEndDate(CalendarUtil.cal2string(Calendar.getInstance()));
        model.addAttribute("applyPreparer", applyPreparer);
        return "leaves/singleApplyForm";
    }

    @Override
    public String sendSingleLeave(Principal principal, LeaveApplyPreparer applyPreparer, BindingResult bindingResult,Model model) {
        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            model.addAttribute("applyPreparer",applyPreparer);
            return sendSingleLeaveErr(model, applyPreparer, principal);
        }
        applyPreparer.setLeaveType(LeaveType.WYPOCZYNKOWY);
        applyPreparer.setUsername(principal.getName());
        applyPreparer.setText("Dzień dobry,\nzwracam się z prośbą o udzielenie urlopu wypoczynkowego w terminie "+ CalendarUtil.singleLeaveDateFormatter(applyPreparer.getStartDate()) +"\n\nPozdrawiam,");
        emailConfiguration.sendAckMail(applyPreparer);
        return "redirect:/dtnetwork";
    }

    public static String getUser(){
        String currentUserName = "Nieznany user";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        return currentUserName;
    }

    public static long betweenDates(Calendar firstDate, Date secondDate){
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }
    public String setForgottenPassErr(Model model, NewPassDTO newPassDTO){
        List<String> users = userDAO.findAll().stream().map(d->d.getUsername()).collect(Collectors.toList());
        model.addAttribute("users",users);
        model.addAttribute("newPass",newPassDTO);
        return "users/setForgottenPassForm";
    }

    public Author string2Aut(String name){
        return Author.valueOf(name);
    }

    private String sendSingleLeaveErr(Model model, LeaveApplyPreparer applyPreparer, Principal principal){

        model.addAttribute("applyPreparer",applyPreparer);
        return "leaves/singleApplyForm";
    }
}
