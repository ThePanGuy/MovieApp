package com.myprojects.demo.services;

import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Role;
import com.myprojects.demo.exceptions.UsernameException;
import com.myprojects.demo.repositories.RoleRepository;
import com.myprojects.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<MovieUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public MovieUser getUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public MovieUser addUser(String username, String password) {
        MovieUser movieUser = new MovieUser();
        movieUser.setUsername(username);
        movieUser.setPassword(password);
        Optional<MovieUser> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new UsernameException("Username: " + username + " already exists.");
        }
        movieUser = userRepository.save(movieUser);
        log.info("New user: {} added.", username);
        return movieUser;
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        Optional<MovieUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Role role = roleRepository.findByName(roleName);
            user.get().getRoles().add(role);
        }
    }
}
