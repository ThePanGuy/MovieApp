package com.myprojects.demo.dto;

import com.myprojects.demo.entities.MovieUser;

import java.time.LocalDateTime;

public record MovieRecord(Long id, String title, String description, LocalDateTime creationDate, MovieUser uploadedBy, Long likes, Long hates) {
}
