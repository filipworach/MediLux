package com.MediLux.MediLux.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class VisitDto {

    private String patientEmail;

    private String employeeEmail;
    private LocalDateTime startTime;
    private LocalDateTime stopTime;

    private String comment;
    private String room;
    private String statusName;
}