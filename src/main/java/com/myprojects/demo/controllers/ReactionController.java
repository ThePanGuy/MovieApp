package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.MovieReactions;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.services.MovieService;
import com.myprojects.demo.services.ReactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/reaction")
public class ReactionController {
    private final ReactionService reactionService;
    private final UserRepository userRepository;
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public ReactionController(ReactionService reactionService, UserRepository userRepository, MovieService movieService, MovieRepository movieRepository) {
        this.reactionService = reactionService;
        this.userRepository = userRepository;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{userId}/likes/{movieId}")
    public MovieReactions likeMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        Reaction reaction = reactionService.likeOrUnlikeMovie(user, movieId);
        return movieService.getMovieReactions(reaction.getMovie());
    }

    @GetMapping("/{userId}/hates/{movieId}")
    public MovieReactions hateMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist"));
        Reaction reaction = reactionService.hateOrUnhateMovie(user, movieId);
        return movieService.getMovieReactions(reaction.getMovie());
    }
}
