package com.myprojects.demo.services;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.dto.MovieReactions;
import com.myprojects.demo.dto.MovieRecord;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<MovieRecord> findAllMovies(PageRequest pageRequest, MovieUser movieUser) {
        if (movieUser == null) {
            return movieRepository.findAllBy(pageRequest);
        }
        return movieRepository.findAllByUploadedByUser(movieUser, pageRequest);
    }

    public MovieReactions getMovieReactions(Movie movie) {
        List<Reaction> reactions = movie.getReactions();
        Long numberOfLikes = reactions.stream().filter(Reaction::getIfLike).count();
        Long numberOfHates = reactions.stream().filter(Reaction::getIfHate).count();
        return new MovieReactions(numberOfLikes, numberOfHates);
    }

    @Transactional
    public Movie addMovie(MovieUser movieUser, MovieForm movieForm) {
        Optional<Movie> movie = movieRepository.findByTitle(movieForm.getTitle());
        if (movie.isPresent()) {
            throw new InvalidInputException("Movie with title: " + movieForm.getTitle() + " already exists.");
        }
        Movie newMovie = new Movie(movieForm);
        newMovie.setUploadedBy(movieUser);
        newMovie = movieRepository.save(newMovie);
        log.info("User with id: {} added movie with title: {}", movieUser.getId(), newMovie.getTitle());
        return newMovie;
    }
}
