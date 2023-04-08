package com.myprojects.demo.services;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
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
}
