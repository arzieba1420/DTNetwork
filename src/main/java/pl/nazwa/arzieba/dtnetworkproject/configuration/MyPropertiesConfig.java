package pl.nazwa.arzieba.dtnetworkproject.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "my")
@Getter
@Setter
@NoArgsConstructor
public class MyPropertiesConfig {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    public  Integer pagesize;
    public  String defaultScheduleURL;

}




