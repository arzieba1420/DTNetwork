package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.users.UserService;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    private UserService userService;

    //---------------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    //--------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    @GetMapping("/{username}")
    public String changePasswordForm(Model model, @PathVariable String username ){
        return userService.changePasswordForm(model, username);
    }

    @PostMapping("/changePass")
    public String changePass(@Valid @ModelAttribute("newPass") NewPassDTO newPassDTO, BindingResult bindingResult, Model model){
        return userService.changePass(newPassDTO, bindingResult, model);
    }

    @PostMapping("/saveDiary/{username}")
    public String saveDiary (Model model, @PathVariable String username, @ModelAttribute("diaryText") String diaryText ){
        return userService.saveDiary(model, username, diaryText);
    }
}
