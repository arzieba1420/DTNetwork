package pl.nazwa.arzieba.dtnetworkproject.services.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.model.User;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    private ShortPostDAO shortPostDAO;

    private DeviceDAO deviceDAO;

    //------------------------------------------------------------CONSTRUCTOR---------------------------------------------------------------------------------------------

    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder, ShortPostDAO shortPostDAO, DeviceDAO deviceDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.shortPostDAO = shortPostDAO;
        this.deviceDAO = deviceDAO;
    }

    //------------------------------------------------------------CONTROLLER METHODS---------------------------------------------------------------------------------------------

    @Override
    public String changePasswordForm(Model model, String username) {
        User user =userDAO.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("newPass", new NewPassDTO());
        return "users/changePasswordForm";
    }

    @Override
    @Transactional
    public String changePass(NewPassDTO newPassDTO, BindingResult bindingResult, Model model) {
        User user = userDAO.findByUsername(newPassDTO.getLogin());

        if(bindingResult.hasFieldErrors()){
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return changePasswordFormErr(model,newPassDTO.getLogin(),newPassDTO);
        }

        if(!passwordEncoder.matches(newPassDTO.getOldPass(),userDAO.findByUsername(newPassDTO.getLogin()).getPassword())){
            List<FieldError> allErrors;
            FieldError fieldError = new FieldError("newPass","oldPass",newPassDTO.getOldPass(),
                    false,null,null,"Niepoprawne aktualne hasło!");
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
                    false,null,null,"Hasła niezgodne!");
            bindingResult.addError(fieldError);
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return changePasswordFormErr(model,newPassDTO.getLogin(),newPassDTO);
        }

        user.setPassword(passwordEncoder.encode(newPassDTO.getNewPass()));
        userDAO.save(user);
        return "redirect:/logout";
    }

    @Override
    public String saveDiary(Model model, String username, String diaryText) {
        User user = userDAO.findByUsername(username);
        user.setPersonalDiary(diaryText);
        userDAO.save(user);

        if(username.equals("DTP")){
            ShortPost post = new ShortPost();
            post.setDevice(deviceDAO.findByInventNumber("S-GENERAL"));
            post.setPostDate(Calendar.getInstance());
            post.setDate(new Date());
            post.setContent("Dokonano zmian w sekcji Zaplanowane Zadania! [SYSTEM]");
            post.setAuthor(Author.DTP);
            post.setPostLevel(PostLevel.INFO);
            shortPostDAO.save(post);
        }

        return "redirect:/dtnetwork";
    }

    public String changePasswordFormErr(Model model,  String username, NewPassDTO newPassDTO ){
        User user =userDAO.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("newPass", newPassDTO);
        return "users/changePasswordForm";
    }
}
