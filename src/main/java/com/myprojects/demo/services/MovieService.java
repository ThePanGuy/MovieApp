package com.myprojects.demo.services;

import com.myprojects.demo.dto.movie.MovieFilter;
import com.myprojects.demo.dto.movie.MovieForm;
import com.myprojects.demo.dto.movie.MovieRecord;
import com.myprojects.demo.dto.movie.MovieTableItem;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
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
    private final MovieFilterService movieFilterService;

    public MovieService(MovieRepository movieRepository,
                        MovieFilterService movieFilterService) {
        this.movieRepository = movieRepository;
        this.movieFilterService = movieFilterService;
    }

    public Page<MovieTableItem> findMovies(PagingRequest pagingRequest) {
        Sort sort = pagingRequest.hasSorting() ? pagingRequest.getSorting() : Sort.by("likes").descending();
        PageRequest pageRequest = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize(), sort);

        MovieFilter filter = new MovieFilter.Builder()
                .setUploadedById(pagingRequest.getLongFilterValue("uploadedBy"))
                .setSort(sort)
                .setPageRequest(pageRequest).build();

        return movieFilterService.filter(filter);
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
