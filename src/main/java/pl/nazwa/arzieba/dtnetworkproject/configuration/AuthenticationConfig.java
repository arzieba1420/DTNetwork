package pl.nazwa.arzieba.dtnetworkproject.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.services.load.LoadService;
import pl.nazwa.arzieba.dtnetworkproject.services.load.LoadServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationConfig extends SimpleUrlAuthenticationSuccessHandler {

    //--------------------------------------------------------------------LOCAL VARIABLES-------------------------------------------------------------------------------------------
    Logger logger = LoggerFactory.getLogger(this.getClass());


    //--------------------------------------------------------------------CLASS METHODS-------------------------------------------------------------------------------------------
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        logger.info("Poprawne logowanie, "+ LoadServiceImpl.getUser());
    }
}
