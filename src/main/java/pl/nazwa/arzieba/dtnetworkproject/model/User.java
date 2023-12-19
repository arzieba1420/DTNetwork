package pl.nazwa.arzieba.dtnetworkproject.model;


import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "AppUser")
@Getter
@Setter
@NoArgsConstructor
public class User {

    //-----------------------------------------------------------------------MODEL FIELDS-----------------------------------------------------------------------------------------

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    private boolean active;
    private Author author;
    private String roles;
    private String permissions;

    @Nullable
    private String personalDiary;

    //-----------------------------------------------------------------------CONSTRUCTOR-----------------------------------------------------------------------------------------

    public User(String username, String password, Author author, String roles, String permissions) {
        this.username = username;
        this.password = password;
        this.author = author;
        this.roles = roles;
        this.permissions = permissions;
        this.active = true;
    }

    public List<String> getRoleList(){

        if (this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<String>();
    }

    public List<String> getPermissionsList(){

        if (this.permissions.length()>0){
            return Arrays.asList(this.permissions.split(","));
        }

        return new ArrayList<String>();
    }
}
