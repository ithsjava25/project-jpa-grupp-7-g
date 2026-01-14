
package org.example;

import org.example.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CarServiceTest {

    private final CarService carService = new CarService();

    @Test
    void testGetAllCarsReturnsNotNull() {
        List<Car> cars = carService.getAllCars();
        assertThat(cars).isNotNull();
    }

    @Test
    void testGetAllCarsReturnsAList() {
        List<Car> cars = carService.getAllCars();
        assertThat(cars).isInstanceOf(List.class);
    }
}
