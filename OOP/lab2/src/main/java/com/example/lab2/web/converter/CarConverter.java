package com.example.lab2.web.converter;


import com.example.lab2.model.Car;
import com.example.lab2.web.model.CarDto;
import org.springframework.stereotype.Component;

@Component
public class CarConverter implements Converter<Car, CarDto> {

    @Override
    public CarDto toDto(Car model) {
        return new CarDto(model.getId(), model.getName(), model.getAvailable(), model.getCostPerDay());
    }

    @Override
    public Car toModel(CarDto dto) {
        return Car.builder().name(dto.getName()).costPerDay(dto.getCostPerDay()).build();
    }
}
