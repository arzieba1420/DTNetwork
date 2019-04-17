package com.arzieba.dtnetworkproject.dto;

import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.model.DeviceType;
import com.arzieba.dtnetworkproject.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component

@Getter
@Setter
@ToString
public class DeviceDTO {

    private String inventNumber;
    private String deviceDescription;
    private Room room;
    private DeviceType deviceType;


}
