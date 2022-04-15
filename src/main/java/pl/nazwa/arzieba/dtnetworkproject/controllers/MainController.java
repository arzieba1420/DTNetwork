package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.DtNetworkApplication;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.services.load.LoadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DamageNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@PropertySource("classpath:application.properties")
@RequestMapping("/")
public class MainController implements ErrorController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ERROR_PATH = "/error";
    public static ApplicationHome applicationHome = new ApplicationHome(DtNetworkApplication.class);
    private ShortPostDAO shortPostDAO;
    private UserDAO userDAO;
    private LoadService loadService;

    //---------------------------------------------------------------------CONSTRUCTOR---------------------------------------------------------------------------------------

    @Autowired
    public MainController(UserDAO userDAO,  ShortPostDAO shortPostDAO, LoadService loadService) {
        this.shortPostDAO = shortPostDAO;
        this.userDAO = userDAO;
        this.loadService = loadService;
    }

    //--------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    @GetMapping
    public String homeLog(Model model) {
        return loadHome(model);
    }

    @GetMapping("/dtnetwork")
    public String loadHome(Model model)  {
        return loadService.loadHome(model);
    }

    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request) {
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/dev/logs")
    private String openLogFile( HttpServletRequest request ) {
        return loadService.openLogFile(request);
    }

    @GetMapping("/dev/exc")
    public String throwTestException(){
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

    @GetMapping(value = ERROR_PATH)
    public String error(){
        return "error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @GetMapping("/dev/users")
    private @ResponseBody List<User> allUsers(){
        return userDAO.findAll();
    }

    @GetMapping("/dev/users/remove")
    private @ResponseBody void removeUsers(){
        userDAO.deleteAll();
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/dev/users/setPass")
    public String setForgottenPass(Model model){
        return loadService.setForgottenPass(model);
    }

    @PostMapping("/dev/setPass")
    public String changePass(@Valid @ModelAttribute("newPass") NewPassDTO newPassDTO, BindingResult bindingResult, Model model){
        return loadService.changePass(newPassDTO, bindingResult, model);
    }



    @GetMapping("/changeID")
    public String changeID(Model model) {
        int i =1;

        for (ShortPost post:shortPostDAO.findAll()){
            post.setPostId(i);
            i++;
        }
        return loadHome(model);
    }
}
