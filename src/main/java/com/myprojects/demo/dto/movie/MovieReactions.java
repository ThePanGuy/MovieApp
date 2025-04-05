package com.myprojects.demo.dto.movie;

import com.myprojects.demo.entities.Movie;

public class MovieReactions {
    private Long numberOfLikes;
    private Long numberOfHates;

    public MovieReactions(Movie movie) {
        this.numberOfHates = movie.getHates();
        this.numberOfLikes = movie.getLikes();
    }

    public Long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Long getNumberOfHates() {
        return numberOfHates;
    }

    public void setNumberOfHates(Long numberOfHates) {
        this.numberOfHates = numberOfHates;
    }
}
