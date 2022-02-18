package me.metropanties.fileserver.service.impl;

import lombok.RequiredArgsConstructor;
import me.metropanties.fileserver.enums.Role;
import me.metropanties.fileserver.model.User;
import me.metropanties.fileserver.repository.UserRepository;
import me.metropanties.fileserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        if (user == null) {
            LOGGER.error("Tried to save a null user!");
            throw new NullPointerException("Can't save a null user!");
        }

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.addRole(Role.USER);
        return repository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(repository.getById(id));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        if (repository.getByUsername(username) == null)
            return Optional.empty();

        return Optional.of(repository.getByUsername(username));
    }

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public boolean validateUsername(String username) {
        for (User user : getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = getUserByUsername(username);
        if (user.isEmpty())
            throw new IllegalStateException("Failed to load user by username!");

        User x = user.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        x.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return new org.springframework.security.core.userdetails.User(x.getUsername(), x.getPassword(), authorities);
    }

}
