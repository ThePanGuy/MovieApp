package com.myprojects.demo.repositories;

import com.myprojects.demo.entities.MovieUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MovieUser, Long> {
    Optional<MovieUser> findUserByUsername(String username);
}
