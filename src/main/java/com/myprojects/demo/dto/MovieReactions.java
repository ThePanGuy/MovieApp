package com.myprojects.demo.dto;

public class MovieReactions {
    private Long numberOfLikes;
    private Long numberOfHates;

    public MovieReactions(Long numberOfLikes, Long numberOfHates) {
        this.numberOfLikes = numberOfLikes;
        this.numberOfHates = numberOfHates;
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
