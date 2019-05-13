package pl.nazwa.arzieba.dtnetworkproject.dto;

import pl.nazwa.arzieba.dtnetworkproject.model.DeviceType;
import pl.nazwa.arzieba.dtnetworkproject.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Component

@Getter
@Setter
@ToString
public class DeviceDTO {

   @NotBlank(
            message = "Invent number cannot be empty!"  )
    private String inventNumber;

    @NotBlank(
            message = "Details cannot be empty!"  )
    private String deviceDescription;

    @NotNull(message = "Room not specified!")
    private Room room;

    @NotNull(message = "Device type not specified!")
    private DeviceType deviceType;
}
