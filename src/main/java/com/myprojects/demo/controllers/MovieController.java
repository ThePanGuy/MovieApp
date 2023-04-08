package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.services.MovieService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final UserRepository userRepository;
    private final MovieService movieService;

    public MovieController(UserRepository userRepository, MovieService movieService) {
        this.userRepository = userRepository;
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies() {
        return movieService.findAllMovies();
    }

    @PostMapping("/{userId}/add")
    public Movie addMovie(@PathVariable Long userId, @RequestBody MovieForm movieForm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        return movieService.addMovie(user, movieForm);
    }
}
