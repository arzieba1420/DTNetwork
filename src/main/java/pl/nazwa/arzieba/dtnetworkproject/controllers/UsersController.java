package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.User;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    private UserDAO userDAO;
    private NewPassDTO newPassDTO;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UsersController(UserDAO userDAO, NewPassDTO newPassDTO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.newPassDTO = newPassDTO;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public String changePasswordForm(Model model, @PathVariable String username ){

        User user =userDAO.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("newPass", new NewPassDTO());
        return "users/changePasswordForm";
    }


    public String changePasswordFormErr(Model model,  String username, NewPassDTO newPassDTO ){

        User user =userDAO.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("newPass", newPassDTO);
        return "users/changePasswordForm";
    }

    @PostMapping("/changePass")
    public String changePass(@Valid @ModelAttribute("newPass") NewPassDTO newPassDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return changePasswordFormErr(model,newPassDTO.getLogin(),newPassDTO);
        }

        if(!passwordEncoder.matches(newPassDTO.getOldPass(),userDAO.findByUsername(newPassDTO.getLogin()).getPassword())){
            List<FieldError> allErrors;
            FieldError fieldError = new FieldError("newPass","oldPass",newPassDTO.getOldPass(),
                    false,null,null,"Incorrect old pass!");

            bindingResult.addError(fieldError);
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return changePasswordFormErr(model,newPassDTO.getLogin(),newPassDTO);
        }

        if (!newPassDTO.getNewPass().equals(newPassDTO.getNewPassConfirmed())){
            List<FieldError> allErrors;
            FieldError fieldError = new FieldError("newPass","newPassConfirmed",newPassDTO.getNewPassConfirmed(),
                    false,null,null,"Passwords are not equal!");

            bindingResult.addError(fieldError);
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return changePasswordFormErr(model,newPassDTO.getLogin(),newPassDTO);
        }

        User user = userDAO.findByUsername(newPassDTO.getLogin());
        user.setPassword(passwordEncoder.encode(newPassDTO.getNewPass()));
        userDAO.save(user);
        return "redirect:/logout";
    }




}
