package com.example.lab2.repository;

import com.example.lab2.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findAllByAvailableIsTrue();

    Optional<Car> findAllByAvailableIsTrueAndId(Integer id);

    List<Car> findAllById(Integer id);
}
