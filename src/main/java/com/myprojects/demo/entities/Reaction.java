package com.myprojects.demo.entities;


import javax.persistence.*;

@Entity
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(generator = "reaction_id_seq")
    @SequenceGenerator(name = "reaction_id_seq", sequenceName = "reaction_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column
    @Enumerated(EnumType.STRING)
    private ReactionType type;

    public Reaction() {
    }

    public Reaction(MovieUser movieUser, Movie movie, ReactionType type) {
        this.movieUser = movieUser;
        this.movie = movie;
        this.type = type;
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

    public ReactionType getType() {
        return type;
    }

    public void setType(ReactionType type) {
        this.type = type;
    }

    @Transient
    public boolean getIfLike() {
        return type != null && type.equals(ReactionType.LIKE);
    }

    @Transient
    public boolean getIfHate() {
        return type != null && type.equals(ReactionType.HATE);
    }

    public enum ReactionType {
        LIKE, HATE
    }
}
