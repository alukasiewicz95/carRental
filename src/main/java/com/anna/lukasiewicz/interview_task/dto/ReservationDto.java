package com.anna.lukasiewicz.interview_task.dto;

import com.anna.lukasiewicz.interview_task.entity.CarType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;

    @NotNull
    private CarType carType;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;
}
