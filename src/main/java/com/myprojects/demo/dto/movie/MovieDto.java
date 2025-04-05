package com.myprojects.demo.dto.movie;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Reaction;

import java.time.LocalDateTime;

public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private MovieUser uploadedBy;
    private Long likes;
    private Long hates;

    public MovieDto() {
    }

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.creationDate = movie.getCreationDate();
        this.uploadedBy = movie.getUploadedBy();
        this.likes = movie.getReactions().stream().filter(Reaction::getIfLike).count();
        this.hates = movie.getReactions().stream().filter(Reaction::getIfHate).count();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public MovieUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(MovieUser uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getHates() {
        return hates;
    }

    public void setHates(Long hates) {
        this.hates = hates;
    }
}
