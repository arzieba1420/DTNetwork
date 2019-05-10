package pl.nazwa.arzieba.dtnetworkproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DtNetworkApplication  {
    public static final Logger LOG = LoggerFactory.getLogger(DtNetworkApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DtNetworkApplication.class, args);
    }

}
