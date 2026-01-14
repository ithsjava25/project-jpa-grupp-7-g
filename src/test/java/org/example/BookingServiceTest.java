package org.example;

import org.example.entity.Addon;
import org.example.entity.BookingType;
import org.example.entity.Car;
import org.example.service.BookingService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingServiceTest {

    private final BookingService bookingService = new BookingService();

    @Test
    void testCalculatePriceHourly() {
        Car car = new Car("Volvo", "XC90", "ABC123", 500, 2000);
        LocalDateTime start = LocalDateTime.of(2026, 1, 14, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 14, 12, 0); // 2 timmar
        List<Addon> addons = new ArrayList<>();

        double price = bookingService.calculatePrice(car, start, end, BookingType.HOURLY, addons);

        // 2 timmar * 500 kr/timme = 1000 kr
        assertThat(price).isEqualTo(1000);
    }

    @Test
    void testCalculatePriceDaily() {
        Car car = new Car("Volvo", "XC90", "ABC123", 500, 2000);
        LocalDateTime start = LocalDateTime.of(2026, 1, 14, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 16, 10, 0); // 2 dagar
        List<Addon> addons = new ArrayList<>();

        double price = bookingService.calculatePrice(car, start, end, BookingType.DAILY, addons);

        // 2 dagar * 2000 kr/dag = 4000 kr
        assertThat(price).isEqualTo(4000);
    }

    @Test
    void testCalculatePriceWithAddons() {
        Car car = new Car("Volvo", "XC90", "ABC123", 500, 2000);
        LocalDateTime start = LocalDateTime.of(2026, 1, 14, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 14, 12, 0); // 2 timmar

        List<Addon> addons = new ArrayList<>();
        Addon addon1 = new Addon("GPS", "GPS-navigator", 100);
        addons.add(addon1);

        double price = bookingService.calculatePrice(car, start, end, BookingType.HOURLY, addons);

        // (2 timmar * 500 kr/timme) + 100 kr addon = 1100 kr
        assertThat(price).isEqualTo(1100);
    }

    @Test
    void testCalculatePriceMinimumOneHour() {
        Car car = new Car("Volvo", "XC90", "ABC123", 500, 2000);
        LocalDateTime start = LocalDateTime.of(2026, 1, 14, 10, 30);
        LocalDateTime end = LocalDateTime.of(2026, 1, 14, 10, 45); // Samma timme
        List<Addon> addons = new ArrayList<>();

        double price = bookingService.calculatePrice(car, start, end, BookingType.HOURLY, addons);

        // Minimum 1 timme även om mindre än 1 timme valts
        assertThat(price).isEqualTo(500);
    }
}
