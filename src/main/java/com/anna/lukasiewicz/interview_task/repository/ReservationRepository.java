package com.anna.lukasiewicz.interview_task.repository;

import com.anna.lukasiewicz.interview_task.entity.CarType;
import com.anna.lukasiewicz.interview_task.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCarTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            CarType type,
            LocalDateTime end,
            LocalDateTime start
    );
}
