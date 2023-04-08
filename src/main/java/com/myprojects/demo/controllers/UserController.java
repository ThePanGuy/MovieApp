package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.UserForm;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{userId}/likes/{movieId}")
    public Reaction likeMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        return userService.likeMovie(user, movieId);
    }

    @GetMapping("/{userId}/hates/{movieId}")
    public Reaction hateMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        return userService.hateMovie(user, movieId);
    }
    @PostMapping("/new-user")
    public String newUser(@RequestBody UserForm userForm) {
        User user = userService.addUser(userForm.getUsername(), userForm.getPassword());
        return String.format("New user: %s added", user.getUsername());
    }
}
