package com.MediLux.MediLux.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitSeriesDto {
    private String startTime;
    private String stopTime;
    private short howLongBreakBetweenVisits;
    private short howLongVisit;
    private String visitType;
    private String employeeEmail;
    private String room;
}
