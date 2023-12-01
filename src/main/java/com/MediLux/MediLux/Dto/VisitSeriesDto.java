package com.MediLux.MediLux.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class VisitSeriesDto {
    private LocalDateTime startTime;
    private LocalDateTime stopTime;
    private short howLongBreakBetweenVisits;
    private short howLongVisit;
    private String visitType;
    private String employeeEmail;
    private String room;
}
