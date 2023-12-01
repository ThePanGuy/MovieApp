package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.dto.MovieRecord;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.requests.PagingRequest;
import com.myprojects.demo.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final UserRepository userRepository;
    private final MovieService movieService;

    public MovieController(UserRepository userRepository, MovieService movieService) {
        this.userRepository = userRepository;
        this.movieService = movieService;
    }

    @PostMapping("/page")
    public Page<MovieRecord> getMoviesPage(@RequestBody PagingRequest pagingRequest) {
        Sort sort = pagingRequest.hasSorting() ? pagingRequest.getSorting() : Sort.by("creationDate").descending();
        PageRequest pageRequest = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize(), sort);

        if (pagingRequest.getFilterValue("uploadedBy") != null) {
            User user = userRepository.findUserByUsername(pagingRequest.getFilterValue("uploadedBy"))
                    .orElseThrow(() -> new InvalidInputException("There is no user with this username"));
            return movieService.findAllMovies(pageRequest, user);
        }
        return movieService.findAllMovies(pageRequest, null);
    }

    @PostMapping("/{userId}/add")
    public Movie addMovie(@PathVariable Long userId, @RequestBody MovieForm movieForm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        return movieService.addMovie(user, movieForm);
    }
}
