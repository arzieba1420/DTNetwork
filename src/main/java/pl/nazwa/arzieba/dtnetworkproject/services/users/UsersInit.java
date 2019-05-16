/*package pl.nazwa.arzieba.dtnetworkproject.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.User;

import java.util.Arrays;
import java.util.List;

@Service
public class UsersInit implements CommandLineRunner {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsersInit(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        this.userDAO.deleteAll();

        User arek = new User("Arek",passwordEncoder.encode("BitDef3500") , Author.Arek,"ADMIN,USER","");
        User admin = new User("admin",passwordEncoder.encode("dtnetwork"), Author.ADMIN,"ADMIN,USER","");
        User defaultUser = new User("DTN",passwordEncoder.encode("DTN1"), Author.DEFAULT,"USER","");

        List<User> users = Arrays.asList(admin,arek,defaultUser);

        userDAO.saveAll(users);

    }
}*/
