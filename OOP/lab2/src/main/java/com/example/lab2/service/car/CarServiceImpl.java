package com.example.lab2.service.car;

import com.example.lab2.model.Car;
import com.example.lab2.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public List<Car> getCars(Boolean isAdmin) {
        return Boolean.TRUE.equals(isAdmin) ? carRepository.findAll() : carRepository.findAllByAvailableIsTrue();
    }

    @Override
    public Boolean assertExistsById(Integer carId) {
        return !carRepository.findAllById(carId).isEmpty();
    }
}
