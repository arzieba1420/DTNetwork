package pl.nazwa.arzieba.dtnetworkproject.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.User;

@Repository
public interface UserDAO extends JpaRepository<User,Long> {

    User findByUsername(String username);

}
