package com.MediLux.MediLux.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitDto {

    private Long id;
    private String patientEmail;

    private String employeeEmail;
    private String name;
    private String surname;
    private String employeeName;
    private String employeeSurname;
    private String startTime;
    private String stopTime;

    private String comment;
    private String room;
    private String statusName;
    private String visitType;
    private byte[] prescription;
}
