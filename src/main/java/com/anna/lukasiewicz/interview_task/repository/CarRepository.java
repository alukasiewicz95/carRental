package com.anna.lukasiewicz.interview_task.repository;

import com.anna.lukasiewicz.interview_task.entity.Car;
import com.anna.lukasiewicz.interview_task.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    long countByType(CarType type);
}
