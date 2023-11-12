package com.myprojects.demo.entities;


import com.myprojects.demo.dto.MovieForm;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(targetEntity = MovieUser.class)
    @JoinColumn(name = "uploadedBy")
    private MovieUser uploadedBy;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Reaction> reactions = new ArrayList<>();

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

    public MovieUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(MovieUser uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}
