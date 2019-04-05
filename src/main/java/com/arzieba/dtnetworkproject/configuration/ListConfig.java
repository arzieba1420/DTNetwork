package com.arzieba.dtnetworkproject.configuration;

import com.arzieba.dtnetworkproject.model.IssueDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ListConfig {


    @Bean
    public ArrayList<IssueDocument> list(){
        return new ArrayList<>();
    }

}
