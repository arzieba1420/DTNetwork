package pl.nazwa.arzieba.dtnetworkproject.services.users;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;

@Service
public interface UserService {

    String changePasswordForm(Model model, String username);
    String changePass(NewPassDTO newPassDTO, BindingResult bindingResult, Model model);
    String saveDiary(Model model, String username, String diaryText);
}
