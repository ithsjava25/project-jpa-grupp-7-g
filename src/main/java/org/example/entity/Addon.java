package org.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addons")
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    @ManyToMany(mappedBy = "addons")
    private List<Booking> bookings = new ArrayList<>();

    public Addon() {}

    public Addon(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Addon(String name, String description, double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " (" + price + " kr)";
    }
}
