package com.myprojects.demo.dto;

import com.myprojects.demo.entities.User;

import java.time.LocalDateTime;

public record MovieRecord(Long id, String title, String description, LocalDateTime creationDate, User uploadedBy, Long likes, Long hates) {
}
