package org.example.service;

import org.example.entity.Car;
import org.example.repository.CarRepository;
import java.util.List;

// CarService - Service class for Car entity

public class CarService {

    private final CarRepository repo = new CarRepository();

    public List<Car> getAllCars() {
        return repo.findAll();
    }
}
