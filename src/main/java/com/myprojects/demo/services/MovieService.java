package com.myprojects.demo.services;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    public Movie addMovie(User user, MovieForm movieForm) {
        Optional<Movie> movie = movieRepository.findByTitle(movieForm.getTitle());
        if (movie.isPresent()) {
            throw new InvalidInputException("Movie with title: " + movieForm.getTitle() + " already exists.");
        }
        Movie newMovie = new Movie(movieForm);
        newMovie.setUploadedBy(user);
        newMovie = movieRepository.save(newMovie);
        log.info("User with id: {} added movie with title: {}", user.getId(), newMovie.getTitle());
        return newMovie;
    }

    public Movie updateMovieReaction(Reaction oldReaction, Reaction.ReactionType reactionType) {
        if (oldReaction.getReactionType() == null) {
            return addMovieReaction(oldReaction.getMovie(), reactionType);
        } else if (oldReaction.getReactionType() == reactionType) {
            return oldReaction.getMovie();
        } else {
            return changeMovieReaction(oldReaction.getMovie(), reactionType);
        }
    }

    public Movie addMovieReaction(Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE:
                movie.addLike();
                break;
            case HATE:
                movie.addHate();
                break;
        }
        return movieRepository.save(movie);
    }

    private Movie changeMovieReaction(Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE:
                movie.removeHate();
                movie.addLike();
                break;
            case HATE:
                movie.removeLike();
                movie.addHate();
        }
        return movieRepository.save(movie);
    }
}
