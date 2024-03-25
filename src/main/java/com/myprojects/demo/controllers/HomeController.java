package com.myprojects.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.demo.dto.DecodedData;
import com.myprojects.demo.dto.UserForm;
import com.myprojects.demo.dto.movie.MoviePage;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Role;
import com.myprojects.demo.requests.PagingRequest;
import com.myprojects.demo.services.MovieService;
import com.myprojects.demo.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myprojects.demo.utilities.JwtUtilities.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final MovieService movieService;
    private final UserService userService;

    public HomeController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @PostMapping("/movies")
    public Page<MoviePage> getMoviesPage(@RequestBody PagingRequest pagingRequest) {
        return movieService.findMovies(pagingRequest);
    }



    @PostMapping("/sign-up")
    public ResponseEntity<MovieUser> signUp(@RequestBody UserForm user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.addUser(user.getUsername(), user.getPassword()));
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                DecodedData data = tryToDecode(refreshToken, true);
                MovieUser user = userService.getUser(data.getUsername());
                List<String> authorities = user.getRoles().stream().map(Role::getName).toList();
                String accessToken = createAccessToken(user.getUsername(), authorities, request.getRequestURL().toString());
                Map<String, String> tokens = generateTokensMap(accessToken, refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new IllegalArgumentException("Refresh token is missing");
        }

    }

}
