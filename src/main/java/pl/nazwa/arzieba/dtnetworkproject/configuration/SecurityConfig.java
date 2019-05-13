package pl.nazwa.arzieba.dtnetworkproject.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableEncryptableProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${my.adminPass}")
    private String adminPass;
    @Value("${my.userPass}")
    private String userPass;

    @Bean
    public UserDetailsService userDetailsService(){


        UserDetails user = User.withDefaultPasswordEncoder()
                .username("DTN")
                .password(userPass)
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password(adminPass)
                .roles("ADMIN","USER")
                .build();



        return new InMemoryUserDetailsManager(user,admin);
    }

    //TODO configure permitions
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/dev").hasRole("ADMIN")

                .anyRequest().hasRole("USER")
               /*.hasAnyRole("USER","ADMIN")*/
                .and()
                .csrf().disable()
                .formLogin().permitAll()
                /*.loginPage("/login2")
                .loginProcessingUrl("/perform_login")
*/
                .and()

                .logout().permitAll()
        ;
    }

}
