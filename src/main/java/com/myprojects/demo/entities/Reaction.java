package com.myprojects.demo.entities;


import javax.persistence.*;

@Entity
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(generator = "reaction_seq")
    @SequenceGenerator(name = "reaction_seq", sequenceName = "reaction_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column
    private Boolean isLike;

    public Reaction() {
    }

    public Reaction(MovieUser movieUser, Movie movie) {
        this.movieUser = movieUser;
        this.movie = movie;
    }

    public Reaction(MovieUser movieUser, Movie movie, Boolean isLike) {
        this.movieUser = movieUser;
        this.movie = movie;
        this.isLike = isLike;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieUser getUser() {
        return movieUser;
    }

    public void setUser(MovieUser movieUser) {
        this.movieUser = movieUser;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    @Transient
    public boolean getIfLike() {
        return isLike != null && isLike;
    }

    @Transient
    public boolean getIfHate() {
        return isLike != null && !isLike;
    }
}
