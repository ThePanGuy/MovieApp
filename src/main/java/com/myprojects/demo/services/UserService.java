package com.myprojects.demo.services;

import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.UsernameException;
import com.myprojects.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Optional<User> existingUser = userRepository.findUserByUsername(username);
        if (existingUser.isPresent()) {
            throw new UsernameException("Username: " + username + " already exists.");
        }
        user = userRepository.save(user);
        log.info("New user: {} added.", username);
        return user;
    }
}
