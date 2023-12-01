package com.myprojects.demo.services;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReactionService reactionService;

    public UserService(UserRepository userRepository, MovieRepository movieRepository, ReactionService reactionService) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reactionService = reactionService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Movie addMovie(User user, MovieForm movieForm) {
        Optional<Movie> movie = movieRepository.findByTitle(movieForm.getTitle());
        if (movie.isPresent()) {
            throw new InvalidInputException("Movie with title: " + movieForm.getTitle() + " already exists.");
        }
        Movie newMovie = new Movie(movieForm);
        newMovie.setUploadedBy(user);
        return movieRepository.save(newMovie);
    }

    @Transactional
    public Reaction likeMovie(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        if (movie.getUploadedBy() == user) {
            throw new InvalidInputException("You can not rate your own movies.");
        }
        movie.addLike();
        movieRepository.save(movie);
        return reactionService.addReaction(user, movie, Reaction.ReactionType.LIKE);
    }
}
