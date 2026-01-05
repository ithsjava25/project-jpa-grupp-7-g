package org.example.model;

import jakarta.persistence.*;


import javax.tools.DocumentationTool;
import javax.xml.stream.Location;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id")
    private Location pickupLocation;

    @ManyToOne
    @JoinColumn(name = "dropoff_location_id")
    private Location dropoffLocation;

    @OneToMany
    @JoinColumn(name = "booking_id")
    private List<Extra> extras;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDate startDate;
    private LocalDate endDate;

    protected Booking() {
        // JPA requires a no-arg constructor
    }

    public Booking(Customer customer, Car car, Location pickupLocation, Location dropoffLocation, LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = BookingStatus.RESERVED;
        this.createdAt = LocalDateTime.now();

    }

}
