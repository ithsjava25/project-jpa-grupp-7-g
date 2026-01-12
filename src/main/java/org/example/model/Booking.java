package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    private Car car;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    private List<Extra> extras = new ArrayList<>();

    public Booking(Customer customer, Car car, LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = BookingStatus.RESERVED;
    }

    public void addExtra(Extra extra) {
        this.extras.add(extra);
    }

    public long getDurationInDays() {
        if (startDate == null || endDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    // Getters och Setters
    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Car getCar() { return car; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<Extra> getExtras() { return extras; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
