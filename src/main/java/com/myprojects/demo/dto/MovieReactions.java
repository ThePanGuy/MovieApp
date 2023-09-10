package com.myprojects.demo.dto;

import com.myprojects.demo.entities.Movie;

public class MovieReactions {
    private Integer numberOfLikes;
    private Integer numberOfHates;

    public MovieReactions(Movie movie) {
        this.numberOfLikes = movie.getLikes();
        this.numberOfHates = movie.getHates();
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getNumberOfHates() {
        return numberOfHates;
    }

    public void setNumberOfHates(Integer numberOfHates) {
        this.numberOfHates = numberOfHates;
    }
}
