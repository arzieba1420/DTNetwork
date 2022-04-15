package pl.nazwa.arzieba.dtnetworkproject.services.load;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import javax.servlet.http.HttpServletRequest;

@Service
public interface LoadService {

    String loadHome(Model model);
    String openLogFile( HttpServletRequest request );
    String setForgottenPass(Model model);
    String changePass( NewPassDTO newPassDTO, BindingResult bindingResult, Model model);
}
