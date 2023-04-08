package com.myprojects.demo.dto;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.User;

public class ReactionDto {
    private Long id;
    private User user;
    private Movie movie;
    private Boolean isLike;
    private Boolean isHate;
}
