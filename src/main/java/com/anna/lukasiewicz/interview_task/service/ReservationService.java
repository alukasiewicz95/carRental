package com.anna.lukasiewicz.interview_task.service;

import com.anna.lukasiewicz.interview_task.dto.ReservationDto;
import com.anna.lukasiewicz.interview_task.entity.Car;
import com.anna.lukasiewicz.interview_task.entity.Reservation;
import com.anna.lukasiewicz.interview_task.exception.NoCarsAvailableException;
import com.anna.lukasiewicz.interview_task.exception.ReservationNotFoundException;
import com.anna.lukasiewicz.interview_task.repository.CarRepository;
import com.anna.lukasiewicz.interview_task.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final CarRepository carRepo;

    public ReservationService(ReservationRepository reservationRepo, CarRepository carRepo) {
        this.reservationRepo = reservationRepo;
        this.carRepo = carRepo;
    }

    public ReservationDto createReservation(ReservationDto dto) {

        LocalDateTime startDate = dto.getStartDate();
        LocalDateTime endDate = dto.getEndDate();

        List<Car> cars = carRepo.findByType(dto.getCarType());

        for (Car car : cars) {

            List<Reservation> conflicts = reservationRepo
                    .findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                            car,
                            endDate,
                            startDate
                    );

            if (conflicts.isEmpty()) {

                Reservation reservation = new Reservation();
                reservation.setStartDate(startDate);
                reservation.setEndDate(endDate);
                reservation.setCar(car);

                Reservation saved = reservationRepo.save(reservation);

                return mapToDto(saved);
            }
        }

        throw new NoCarsAvailableException("No cars available for this period");
    }

    public List<ReservationDto> getAllReservations() {
        return reservationRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepo.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        return mapToDto(reservation);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepo.existsById(id)) {
            throw new ReservationNotFoundException("Reservation not found");
        }
        reservationRepo.deleteById(id);
    }

    private ReservationDto mapToDto(Reservation reservation) {

        return new ReservationDto(
                reservation.getId(),
                reservation.getCar().getType(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );
    }
}