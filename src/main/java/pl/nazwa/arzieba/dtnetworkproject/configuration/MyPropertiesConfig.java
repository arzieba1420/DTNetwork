package pl.nazwa.arzieba.dtnetworkproject.configuration;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.DtNetworkApplication;

@Configuration
@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "my")
@Getter
@Setter
@NoArgsConstructor
public class MyPropertiesConfig {
    public  Integer pagesize;
    public  String defaultScheduleURL;



    }




