package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    @ManyToOne
    private Car car;

    @ManyToMany
    private List<Addon> addons = new ArrayList<>();

    private double totalPrice;

    public Booking() {}

    public Booking(LocalDateTime startTime, LocalDateTime endTime, BookingType bookingType, Car car) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingType = bookingType;
        this.car = car;
    }

    public Long getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public BookingType getBookingType() { return bookingType; }
    public void setBookingType(BookingType bookingType) { this.bookingType = bookingType; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public List<Addon> getAddons() { return addons; }
    public void setAddons(List<Addon> addons) { this.addons = addons; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
