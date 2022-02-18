package me.metropanties.fileserver.service;

import me.metropanties.fileserver.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    void deleteUser(Long id);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    List<User> getUsers();

    boolean validateUsername(String username);

}
