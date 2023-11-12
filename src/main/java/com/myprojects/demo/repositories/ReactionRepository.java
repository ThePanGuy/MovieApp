package com.myprojects.demo.repositories;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.MovieUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    public Optional<Reaction> findByUserAndMovie(MovieUser movieUser, Movie movie);
}
