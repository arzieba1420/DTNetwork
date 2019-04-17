package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Author;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ShortPostDTO {

    private Author author;
    private String content;
    private String date;
    private String inventNumber;

}
