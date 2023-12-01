package com.myprojects.demo.dto;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;

public class ReactionDto {
    private Long id;
    private MovieUser movieUser;
    private Movie movie;
    private Boolean isLike;
    private Boolean isHate;
}
