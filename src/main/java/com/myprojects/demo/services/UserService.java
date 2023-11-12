package com.myprojects.demo.services;

import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Role;

import java.util.List;

public interface UserService {
    List<MovieUser> getAllUsers();
    MovieUser getUser(String username);
    MovieUser addUser(String username, String password);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
