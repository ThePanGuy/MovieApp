package com.myprojects.demo.controllers.secured;

import com.myprojects.demo.dto.MovieForm;
import com.myprojects.demo.dto.MovieRecord;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.requests.PagingRequest;
import com.myprojects.demo.services.MovieService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/secured/movie")
public class MovieController {

    private final UserRepository userRepository;
    private final MovieService movieService;

    public MovieController(UserRepository userRepository, MovieService movieService) {
        this.userRepository = userRepository;
        this.movieService = movieService;
    }

    @PostMapping("/page")
    public Object getMoviePage(@AuthenticationPrincipal MovieUser user, @RequestBody PagingRequest pagingRequest) {
        Sort sort = pagingRequest.hasSorting() ? pagingRequest.getSorting() : Sort.by("creationDate").descending();
        PageRequest pageRequest = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize(), sort);

        if (pagingRequest.getFilterValue("uploadedBy") != null) {
            MovieUser movieUser = userRepository.findByUsername(pagingRequest.getFilterValue("uploadedBy"))
                    .orElseThrow(() -> new InvalidInputException("There is no user with this username"));
            return movieService.findAllMovies(pageRequest, movieUser);
        }
        return movieService.findAllMovies(pageRequest, null);
    }

    @PostMapping("/add")
    public MovieRecord addMovie(@AuthenticationPrincipal MovieUser user, @RequestBody MovieForm movieForm) {
        return movieService.addMovie(user, movieForm);
    }
}
