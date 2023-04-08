package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.entities.Movie;
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

    @PostMapping("/{userId}/add")
    public Movie addMovie(@PathVariable Long userId, @RequestParam MovieForm movieForm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        return userService.addMovie(user, movieForm);
    }

    @GetMapping("/{userId}/likes/{movieId}")
    public Reaction likeMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        return userService.likeMovie(user, movieId);
    }
}
