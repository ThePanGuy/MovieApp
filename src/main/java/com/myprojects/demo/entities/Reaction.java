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
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column
    private Boolean isLike;

    public Reaction() {
    }

    public Reaction(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    public Reaction(User user, Movie movie, Boolean isLike) {
        this.user = user;
        this.movie = movie;
        this.isLike = isLike;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
