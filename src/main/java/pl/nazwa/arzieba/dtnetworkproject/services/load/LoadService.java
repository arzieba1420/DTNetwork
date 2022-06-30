package pl.nazwa.arzieba.dtnetworkproject.services.load;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.LeaveApplyPreparer;
import pl.nazwa.arzieba.dtnetworkproject.dto.NewPassDTO;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Service
public interface LoadService {

    String loadHome(Model model, Principal principal);
    String openLogFile( HttpServletRequest request );
    String setForgottenPass(Model model);
    String changePass( NewPassDTO newPassDTO, BindingResult bindingResult, Model model);

    String prepareSingleApplyMail(Model model, Principal principal);

    String sendSingleLeave(Principal principal, LeaveApplyPreparer applyPreparer, BindingResult bindingResult,Model model);
}
