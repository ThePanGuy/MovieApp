package com.myprojects.demo.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "movie_user")
public class User {
    @Id
    @GeneratedValue(generator = "movie_user_seq")
    @SequenceGenerator(name = "movie_user_seq", sequenceName = "movie_user_seq", allocationSize = 1)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    public User() {
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
