package com.anna.lukasiewicz.interview_task.controller;

import com.anna.lukasiewicz.interview_task.dto.ReservationDto;
import com.anna.lukasiewicz.interview_task.exception.NoCarsAvailableException;
import com.anna.lukasiewicz.interview_task.exception.ReservationNotFoundException;
import com.anna.lukasiewicz.interview_task.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationDto> getAll() {
        return service.getAllReservations();
    }

    @PostMapping
    public ReservationDto reserve(@Valid @RequestBody ReservationDto dto) {
        return service.createReservation(dto);
    }

    @GetMapping("/{id}")
    public ReservationDto getById(@PathVariable Long id) {
        return service.getReservationById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteReservation(id);
    }
    @ExceptionHandler(NoCarsAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoCars(NoCarsAvailableException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ReservationNotFoundException ex) {
        return ex.getMessage();
    }

}