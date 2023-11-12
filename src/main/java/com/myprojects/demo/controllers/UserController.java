package com.myprojects.demo.controllers;

import com.myprojects.demo.dto.UserForm;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<MovieUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/new-user")
    public String newUser(@RequestBody UserForm userForm) {
        MovieUser movieUser = userService.addUser(userForm.getUsername(), userForm.getPassword());
        return String.format("New user: %s added", movieUser.getUsername());
    }
}
