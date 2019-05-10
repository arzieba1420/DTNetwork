package pl.nazwa.arzieba.dtnetworkproject.configuration;

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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("DTN")
                .password("DTN1")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password(("dtnetwork"))
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
                .formLogin().permitAll()
                /*.loginPage("/login2")
                .loginProcessingUrl("/perform_login")
*/
                .and()
                .logout().permitAll();
    }

}
