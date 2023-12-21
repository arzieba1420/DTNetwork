package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.utils.annotations.MultipartSelected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadItemDTO {
    private Long id;
    @NotBlank(message = "Podaj opis pliku!"  )
    private String description;
    private String name;
    private String type;
    @MultipartSelected(message = "Wybierz plik!")
    private MultipartFile file;
}
