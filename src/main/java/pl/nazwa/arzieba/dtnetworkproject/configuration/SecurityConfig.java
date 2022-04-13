package pl.nazwa.arzieba.dtnetworkproject.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import pl.nazwa.arzieba.dtnetworkproject.controllers.MainController;
import pl.nazwa.arzieba.dtnetworkproject.services.users.UserPrincipalDetailsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@EnableWebSecurity
@EnableEncryptableProperties
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService){
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider());   }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/dtnetwork").hasAuthority("ROLE_USER")
                .antMatchers("/dev/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().hasAuthority("ROLE_USER")
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    logger.info("Zalogowano pomyślnie: "+authentication.getName());
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/dtnetwork");
                    })
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(new ForwardLogoutSuccessHandler("/login"){
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                        logger.info("Wylogowano pomyslnie: "+authentication.getName());
                        response.sendRedirect("/login");
                    }})
                .and().headers().frameOptions().disable();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }
}
