package pl.nazwa.arzieba.dtnetworkproject.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "my")
@Getter
@Setter
@NoArgsConstructor
public class MyPropertiesConfig {
    public static Integer pagesize;
}
