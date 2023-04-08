package com.myprojects.demo.entities;


import javax.persistence.*;

@Entity
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(generator = "reaction_seq")
    @SequenceGenerator(name = "reaction_seq", sequenceName = "reaction_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column
    private Boolean isLike;

    @Column
    private Boolean isHate;

    public Reaction() {
    }

    public Reaction(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
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

    public Boolean getIsHate() {
        return isHate;
    }

    public void setIsHate(Boolean isHate) {
        this.isHate = isHate;
    }

    @Transient
    public void undo() {
        setIsLike(false);
        setIsHate(false);
    }

    public enum ReactionType {
        LIKE,
        HATE
    }

}
