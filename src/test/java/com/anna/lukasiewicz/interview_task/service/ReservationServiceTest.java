package com.anna.lukasiewicz.interview_task.service;

import com.anna.lukasiewicz.interview_task.dto.ReservationDto;
import com.anna.lukasiewicz.interview_task.entity.CarType;
import com.anna.lukasiewicz.interview_task.entity.Reservation;
import com.anna.lukasiewicz.interview_task.repository.CarRepository;
import com.anna.lukasiewicz.interview_task.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReservationServiceTest {

	@Test
	void shouldThrowExceptionWhenNoCarsAvailable() {
		CarRepository carRepo = Mockito.mock(CarRepository.class);
		ReservationRepository reservationRepo = Mockito.mock(ReservationRepository.class);

		Mockito.when(carRepo.countByType(CarType.SEDAN)).thenReturn(1L);
		Mockito.when(reservationRepo.findByCarTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				Mockito.any(), Mockito.any(), Mockito.any()
		)).thenReturn(java.util.List.of(new Reservation()));

		ReservationService service = new ReservationService(reservationRepo, carRepo);

		ReservationDto dto = new ReservationDto(
				null,
				CarType.SEDAN,
				LocalDateTime.now(),
				LocalDateTime.now()
		);

		assertThrows(RuntimeException.class, () -> service.createReservation(dto));
	}
}
