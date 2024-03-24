package com.myprojects.demo.controllers.secured;

import com.myprojects.demo.dto.movie.MovieForm;
import com.myprojects.demo.dto.movie.MovieRecord;
import com.myprojects.demo.dto.movie.MovieTableItem;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.requests.PagingRequest;
import com.myprojects.demo.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/secured/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/page")
    public Page<MovieTableItem> getMoviePage(@AuthenticationPrincipal MovieUser user, @RequestBody PagingRequest pagingRequest) {
        return movieService.findMovies(pagingRequest);
    }

    @PostMapping("/add")
    public MovieRecord addMovie(@AuthenticationPrincipal MovieUser user, @RequestBody MovieForm movieForm) {
        return movieService.addMovie(user, movieForm);
    }
}
