package com.myprojects.demo.entities;


import com.myprojects.demo.dto.MovieForm;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(generator = "movie_seq")
    @SequenceGenerator(name = "movie_seq", sequenceName = "movie_seq", allocationSize = 1)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private LocalDateTime creationDate;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "uploadedBy")
    private User uploadedBy;

    @Column
    private Integer likes;

    @Column
    private Integer hates;

    public Movie() {
    }

    public Movie(MovieForm movieForm) {
        title = movieForm.getTitle();
        description = movieForm.getDescription();
        creationDate = LocalDateTime.now();
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

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getHates() {
        return hates;
    }

    public void setHates(Integer hates) {
        this.hates = hates;
    }

    @Transient
    public void addLike() {
        this.likes += 1;
    }
}
