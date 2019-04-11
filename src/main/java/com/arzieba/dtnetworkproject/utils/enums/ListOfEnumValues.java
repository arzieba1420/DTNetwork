package com.arzieba.dtnetworkproject.utils.enums;

import com.arzieba.dtnetworkproject.model.DeviceType;
import com.arzieba.dtnetworkproject.model.Room;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListOfEnumValues {

    public static List<String> deviceTypes = Stream.of(DeviceType.values())
            .map(DeviceType::name)
            .collect(Collectors.toList());

    public static List<String> rooms = Stream.of(Room.values())
            .map(Room::name)
            .collect(Collectors.toList());

}
