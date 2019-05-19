/*
package pl.nazwa.arzieba.dtnetworkproject.services.users;

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

        userDAO.deleteAll();

        User arek = new User("Arek",passwordEncoder.encode("arek1") , Author.Arek,"USER,ADMIN","");
        User admin = new User("admin",passwordEncoder.encode("dtnetwork") , Author.ADMIN,"USER,ADMIN","");
        User dtn = new User("DTN",passwordEncoder.encode("DTN1") , Author.DEFAULT,"USER","");



        User artur = new User("Artur",passwordEncoder.encode("artur1") , Author.Artur,"USER","");
        User bartek = new User("Bartek",passwordEncoder.encode("bartek1") , Author.Bartek,"USER","");
        User grzesiek = new User("Grzesiek",passwordEncoder.encode("grzesiek1") , Author.Grzesiek,"USER","");
        User damian = new User("Damian",passwordEncoder.encode("damian1") , Author.Damian,"USER","");
        User sebastian = new User("Sebastian",passwordEncoder.encode("sebastian1") , Author.Sebastian,"USER","");
        User rafał = new User("Rafał",passwordEncoder.encode("rafał1") , Author.Rafał,"USER","");
        User tomek = new User("Tomek",passwordEncoder.encode("tomek1") , Author.Tomek,"USER","");

        List<User> users = Arrays.asList(arek,bartek,grzesiek,damian,sebastian,rafał,tomek,artur,admin,dtn);

        userDAO.saveAll(users);

    }
}
*/
