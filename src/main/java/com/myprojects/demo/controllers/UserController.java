package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.MovieReactions;
import com.myprojects.demo.dto.UserForm;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.services.MovieService;
import com.myprojects.demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final MovieService movieService;

    public UserController(UserService userService, UserRepository userRepository, MovieService movieService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{userId}/likes/{movieId}")
    public MovieReactions likeMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        Reaction reaction = userService.likeOrUnlikeMovie(user, movieId);
        return movieService.getMovieReactions(reaction.getMovie());
    }

    @GetMapping("/{userId}/hates/{movieId}")
    public MovieReactions hateMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        Reaction reaction = userService.hateOrUnhateMovie(user, movieId);
        return movieService.getMovieReactions(reaction.getMovie());
    }

    @PostMapping("/new-user")
    public String newUser(@RequestBody UserForm userForm) {
        User user = userService.addUser(userForm.getUsername(), userForm.getPassword());
        return String.format("New user: %s added", user.getUsername());
    }
}
