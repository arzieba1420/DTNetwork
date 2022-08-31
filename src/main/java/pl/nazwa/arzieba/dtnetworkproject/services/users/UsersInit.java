/*

package pl.nazwa.arzieba.dtnetworkproject.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.GeneratorTestDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.*;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceServiceImpl;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class UsersInit implements CommandLineRunner {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private DeviceDAO deviceDAO;
    private GeneratorTestDAO generatorTestDAO;
    private ShortPostDAO shortPostDAO;

    @Autowired
    public UsersInit(UserDAO userDAO, PasswordEncoder passwordEncoder, DeviceDAO deviceDAO, GeneratorTestDAO generatorTestDAO, ShortPostDAO shortPostDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.deviceDAO = deviceDAO;
        this.generatorTestDAO = generatorTestDAO;
        this.shortPostDAO = shortPostDAO;
    }

    @Override
    public void run(String... args) throws Exception {

        userDAO.deleteAll();


        User admin = new User("admin",passwordEncoder.encode("dtnetwork") , Author.admin,"USER,ADMIN","");
        User dtp = new User("DTP",passwordEncoder.encode("DTP1") , Author.DTP,"USER","");
        User mariusz = new User("Mariusz",passwordEncoder.encode("mariusz1") , Author.Mariusz,"USER","");
        User mateusz = new User("Mateusz",passwordEncoder.encode("mateusz1") , Author.Mateusz,"USER","");
        User grzegorz = new User("Grzegorz",passwordEncoder.encode("grzegorz1") , Author.Grzegorz,"USER","");
        User krzysztofs = new User("KrzysztofS",passwordEncoder.encode("krzysztofS1") , Author.KrzysztofS,"USER","");
        User krzysztofr = new User("KrzysztofR",passwordEncoder.encode("krzysztofR1") , Author.KrzysztofR,"USER","");
        User przemek = new User("Przemek",passwordEncoder.encode("przemek1") , Author.Przemek,"USER","");
        User wacław = new User("Wacław",passwordEncoder.encode("wacław1") , Author.Wacław,"USER","");
        User wojciech = new User("Wojciech",passwordEncoder.encode("wojciech1") , Author.Wojciech,"USER","");
        List<User> users = Arrays.asList(admin,dtp,mariusz,mateusz,grzegorz,krzysztofr,krzysztofs,przemek,wacław);
        userDAO.saveAll(users);

        IntStream.range(1,3).forEach(i->{
            Device device = new Device();
            device.setDeviceType(DeviceType.GENERATOR);
            device.setDeviceDescription("Generator "+i);
            device.setRoom(Room.Generatory);
            device.setInventNumber("S-00"+i);

            GeneratorTest generatorTest = new GeneratorTest();
            generatorTest.setLossPowerFlag(true);
            generatorTest.setContent("SYSTEM ENTRY - DO NOT REMOVE");
            generatorTest.setDate(Calendar.getInstance());
            generatorTest.setAlerted(true);
            generatorTest.setDevice(device);
            generatorTest.setAuthors(Arrays.asList(Author.DTP,Author.admin));
            generatorTest.setStatus(ListOfEnumValues.statuses.get(new Random().nextInt(3)));

            deviceDAO.save(device);
            generatorTestDAO.save(generatorTest);

        });

        Device device = new Device();
        device.setDeviceType(DeviceType.OTHER);
        device.setDeviceDescription("Ogólne");
        device.setRoom(Room.Inne);
        device.setInventNumber("S-GENERAL");
        deviceDAO.save(device);

    }
}

*/
