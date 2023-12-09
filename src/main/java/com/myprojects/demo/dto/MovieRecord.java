package com.myprojects.demo.dto;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Reaction;

import java.time.LocalDateTime;

public record MovieRecord(Long id, String title, String description, LocalDateTime creationDate, MovieUser uploadedBy,
                          Long likes, Long hates) {
    public MovieRecord(Movie movie) {
        this(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getCreationDate(), movie.getUploadedBy(),
                movie.getReactions().stream().filter(Reaction::getIfLike).count(),
                movie.getReactions().stream().filter(Reaction::getIfHate).count());
    }
}
