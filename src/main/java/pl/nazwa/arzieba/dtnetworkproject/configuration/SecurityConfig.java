package pl.nazwa.arzieba.dtnetworkproject.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import pl.nazwa.arzieba.dtnetworkproject.services.users.UserPrincipalDetailsService;

@Configuration
@EnableWebSecurity
@EnableEncryptableProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${my.adminPass}")
    private String adminPass;
    @Value("${my.userPass}")
    private String userPass;
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService){
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth){

        auth.authenticationProvider(authenticationProvider());

    }

    //TODO configure permitions
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/dtnetwork").hasAuthority("ROLE_USER")
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()


                .anyRequest().authenticated()

               /*.hasAnyRole("USER","ADMIN")*/
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")

                .permitAll()

                .and()

                .logout().permitAll()
        ;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);
        return daoAuthenticationProvider;

    }

}
