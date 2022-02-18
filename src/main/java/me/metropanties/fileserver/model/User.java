package me.metropanties.fileserver.model;

import lombok.*;
import me.metropanties.fileserver.enums.Role;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name = "User")
@Table(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @CreatedDate
    private Date createDate = new Date();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addRole(Role role) {
        if (role == null)
            return;

        roles.add(role);
    }

}
