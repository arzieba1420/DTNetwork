package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadItemDTO {
    private Long id;
    private String description;
    private String name;
    private String type;
    private MultipartFile file;
}
