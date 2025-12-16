package org.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();

    // getters & setters
}
