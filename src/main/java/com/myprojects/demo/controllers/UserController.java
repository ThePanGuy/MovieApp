package com.myprojects.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.demo.dto.DecodedData;
import com.myprojects.demo.dto.RoleToUserForm;
import com.myprojects.demo.dto.UserForm;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Role;
import com.myprojects.demo.services.UserService;
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
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieUser>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<MovieUser> saveUser(@RequestBody UserForm user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.addUser(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-user")
    public String newUser(@RequestBody UserForm userForm) {
        MovieUser movieUser = userService.addUser(userForm.getUsername(), userForm.getPassword());
        return String.format("New user: %s added", movieUser.getUsername());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                DecodedData data = tryToDecode(refresh_token, true);
                MovieUser user = userService.getUser(data.getUsername());
                List<String> authorities = user.getRoles().stream().map(Role::getName).toList();
                String access_token = createAccessToken(user.getUsername(), authorities, request.getRequestURL().toString());
                Map<String, String> tokens = generateTokensMap(access_token, refresh_token);
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
            throw new RuntimeException("Refresh token is missing");
        }

    }
}
