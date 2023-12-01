package com.myprojects.demo.dto;

public class MovieForm {
    private String title;
    private String description;
    private Boolean reaction;

    public MovieForm() {
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

    public Boolean getReaction() {
        return reaction;
    }

    public void setReaction(Boolean reaction) {
        this.reaction = reaction;
    }
}
