package pl.nazwa.arzieba.dtnetworkproject.dto;

import pl.nazwa.arzieba.dtnetworkproject.model.DeviceType;
import pl.nazwa.arzieba.dtnetworkproject.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Component

@Getter
@Setter
@ToString
public class DeviceDTO {

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$",
            message = "Invent number cannot be empty!"  )
    private String inventNumber;

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$",
            message = "Device description cannot be empty!"  )
    private String deviceDescription;

    @NotNull(message = "Room not specified!")
    private Room room;

    @NotNull(message = "Device type not specified!")
    private DeviceType deviceType;
}