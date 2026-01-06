package org.example.model;

import jakarta.persistence.*;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    protected Location() {}

    public Location(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

