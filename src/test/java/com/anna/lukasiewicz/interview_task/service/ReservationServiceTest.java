package com.anna.lukasiewicz.interview_task.service;

import com.anna.lukasiewicz.interview_task.dto.ReservationDto;
import com.anna.lukasiewicz.interview_task.entity.Car;
import com.anna.lukasiewicz.interview_task.entity.CarType;
import com.anna.lukasiewicz.interview_task.entity.Reservation;
import com.anna.lukasiewicz.interview_task.exception.NoCarsAvailableException;
import com.anna.lukasiewicz.interview_task.exception.ReservationNotFoundException;
import com.anna.lukasiewicz.interview_task.repository.CarRepository;
import com.anna.lukasiewicz.interview_task.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

	private CarRepository carRepo;
	private ReservationRepository reservationRepo;
	private ReservationService service;

	@BeforeEach
	void setUp() {
		carRepo = Mockito.mock(CarRepository.class);
		reservationRepo = Mockito.mock(ReservationRepository.class);
		service = new ReservationService(reservationRepo, carRepo);
	}

	@Test
	void shouldCreateReservationSuccessfully() {
		ReservationDto dto = new ReservationDto(
				null,
				CarType.SEDAN,
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1)
		);

		Car car1 = new Car();
		car1.setId(1L);
		car1.setType(CarType.SEDAN);

		when(carRepo.findByType(CarType.SEDAN)).thenReturn(List.of(car1));
		when(reservationRepo.findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				eq(car1), any(), any()
		)).thenReturn(List.of());

		Reservation saved = new Reservation();
		saved.setId(1L);
		saved.setStartDate(dto.getStartDate());
		saved.setEndDate(dto.getEndDate());
		saved.setCar(car1);

		when(reservationRepo.save(any())).thenReturn(saved);

		ReservationDto result = service.createReservation(dto);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(CarType.SEDAN, result.getCarType());
	}

	@Test
	void shouldThrowExceptionWhenNoCarsAvailable() {
		ReservationDto dto = new ReservationDto(
				null,
				CarType.SEDAN,
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1)
		);

		Car car1 = new Car();
		car1.setId(1L);
		car1.setType(CarType.SEDAN);

		when(carRepo.findByType(CarType.SEDAN)).thenReturn(List.of(car1));
		when(reservationRepo.findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				eq(car1), any(), any()
		)).thenReturn(List.of(new Reservation()));

		assertThrows(NoCarsAvailableException.class, () -> service.createReservation(dto));
	}

	@Test
	void shouldReturnAllReservations() {
		Car car1 = new Car();
		car1.setId(1L);
		car1.setType(CarType.SEDAN);

		Reservation reservation = new Reservation();
		reservation.setId(1L);
		reservation.setStartDate(LocalDateTime.now());
		reservation.setEndDate(LocalDateTime.now().plusDays(1));
		reservation.setCar(car1);

		when(reservationRepo.findAll()).thenReturn(List.of(reservation));

		List<ReservationDto> result = service.getAllReservations();

		assertEquals(1, result.size());
		assertEquals(CarType.SEDAN, result.get(0).getCarType());
	}

	@Test
	void shouldReturnReservationById() {
		Car car1 = new Car();
		car1.setId(1L);
		car1.setType(CarType.SEDAN);

		Reservation reservation = new Reservation();
		reservation.setId(1L);
		reservation.setStartDate(LocalDateTime.now());
		reservation.setEndDate(LocalDateTime.now().plusDays(1));
		reservation.setCar(car1);

		when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));

		ReservationDto result = service.getReservationById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
	}

	@Test
	void shouldThrowWhenReservationNotFoundById() {
		when(reservationRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ReservationNotFoundException.class,
				() -> service.getReservationById(1L));
	}

	@Test
	void shouldDeleteReservation() {
		when(reservationRepo.existsById(1L)).thenReturn(true);

		service.deleteReservation(1L);

		verify(reservationRepo).deleteById(1L);
	}

	@Test
	void shouldThrowWhenDeletingNonExistingReservation() {
		when(reservationRepo.existsById(1L)).thenReturn(false);

		assertThrows(ReservationNotFoundException.class,
				() -> service.deleteReservation(1L));
	}
}