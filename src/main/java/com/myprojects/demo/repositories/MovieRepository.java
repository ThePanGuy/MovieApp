package com.myprojects.demo.repositories;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);

    Page<Movie> findAllByUploadedBy(User user, Pageable pageable);
}
