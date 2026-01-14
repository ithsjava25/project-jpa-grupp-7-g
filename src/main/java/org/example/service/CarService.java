package org.example.service;

import org.example.model.Car;
import org.example.repository.CarRepository;
import java.util.List;

public class CarService {

    private final CarRepository carRepository = new CarRepository();

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findAvailableCars();
    }

    public void markAsDamaged(Car car) {
        car.setDamaged(true);
        car.setAvailable(false);
        carRepository.save(car);
    }

    public void updateCarStatus(Car car, boolean available) {
        car.setAvailable(available);
        carRepository.save(car);
    }
}
