package pl.nazwa.arzieba.dtnetworkproject.utils.enums;

import pl.nazwa.arzieba.dtnetworkproject.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ListOfEnumValues {

    public static List<DeviceType> deviceTypes = Stream.of(DeviceType.values())
                                                .collect(Collectors.toList());

    public static List<String> rooms = Stream.of(Room.values())
            .map(Room::name)
            .collect(Collectors.toList());

    public static List<String> authors = Stream.of(Author.values())
            .map(Author::name)
            .collect(Collectors.toList());

    public static List<Status> statuses = Stream.of(Status.values()).collect(Collectors.toList());

    public static List<BuildingType> buildings = Stream.of(BuildingType.values()).collect(Collectors.toList());

}
