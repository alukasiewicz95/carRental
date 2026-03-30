package com.anna.lukasiewicz.interview_task.repository;

import com.anna.lukasiewicz.interview_task.entity.Car;
import com.anna.lukasiewicz.interview_task.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByType(CarType type);
}