package com.example.lab2.service.car;

import com.example.lab2.model.Car;

import java.util.List;

public interface CarService {

    List<Car> getCars(Boolean isAdmin);

    Boolean assertExistsById(Integer carId);

}
