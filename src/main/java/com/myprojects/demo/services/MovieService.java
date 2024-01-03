package com.myprojects.demo.services;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.dto.MovieRecord;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.requests.PagingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MovieService {
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public Page<MovieRecord> findMovies(PagingRequest pagingRequest) {
        Sort sort = pagingRequest.hasSorting() ? pagingRequest.getSorting() : Sort.by("creationDate").descending();
        PageRequest pageRequest = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize(), sort);

        MovieUser movieUser = null;
        if (pagingRequest.getFilterValue("uploadedBy") != null) {
            movieUser = userRepository.findByUsername(pagingRequest.getFilterValue("uploadedBy"))
                    .orElseThrow(() -> new InvalidInputException("There is no user with this username"));
        }

        if (movieUser == null) {
            return movieRepository.findAllBy(pageRequest);
        }

        return movieRepository.findAllByUploadedBy(movieUser, pageRequest)
                .map(MovieRecord::new);
    }

    @Transactional
    public MovieRecord addMovie(MovieUser movieUser, MovieForm movieForm) {
        Optional<Movie> movie = movieRepository.findByTitle(movieForm.getTitle());
        if (movie.isPresent()) {
            throw new InvalidInputException("Movie with title: " + movieForm.getTitle() + " already exists.");
        }
        Movie newMovie = new Movie(movieForm);
        newMovie.setUploadedBy(movieUser);
        newMovie = movieRepository.save(newMovie);
        log.info("User with id: {} added movie with title: {}", movieUser.getId(), newMovie.getTitle());
        return new MovieRecord(newMovie);
    }
}
