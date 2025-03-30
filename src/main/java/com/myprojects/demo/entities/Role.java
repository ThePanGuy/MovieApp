package com.myprojects.demo.entities;

import javax.persistence.*;

@Entity
@Table(name = "movie_role")
public class Role {
    @Id
    @GeneratedValue(generator = "movie_role_id_seq")
    @SequenceGenerator(name = "movie_role_id_seq", sequenceName = "movie_role_id_seq", allocationSize = 1)
    private Long id;

    @Column
    private String name;

    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
