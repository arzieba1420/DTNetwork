package pl.nazwa.arzieba.dtnetworkproject.dto;

import pl.nazwa.arzieba.dtnetworkproject.model.DeviceType;
import pl.nazwa.arzieba.dtnetworkproject.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Component

@Getter
@Setter
@ToString
public class DeviceDTO {

    @NotBlank(message = "Podaj numer inwentaryzacyjny!"  )
    private String inventNumber;
    @NotBlank(message = "Podaj opis urządzenia!"  )
    private String deviceDescription;
    @NotNull(message = "Podaj halę!")
    private Room room;
    @NotNull(message = "Podaj typ urządzenia")
    private DeviceType deviceType;
}
