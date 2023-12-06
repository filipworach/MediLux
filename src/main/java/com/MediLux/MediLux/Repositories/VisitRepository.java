package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.*;
import liquibase.pro.packaged.V;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientEmail(String email);
    List<Visit> findByEmployeeAndStopTimeAfterAndStartTimeBefore(Employee doctor, LocalDateTime startTime, LocalDateTime stopTime);
    List<Visit> findByVisitTypeAndStopTimeAfterAndStartTimeBefore(VisitType visitType, LocalDateTime stopTime, LocalDateTime startTime);
    List<Visit> findByVisitTypeAndStopTimeAfterAndStartTimeBeforeAndEmployee(VisitType visitType, LocalDateTime stopTime, LocalDateTime startTime, Employee employee);
    List<Visit> findByEmployeeAndStatus(Employee employee, Status status);
    List<Visit> findByVisitTypeAndStopTimeAfterAndStartTimeBeforeAndPatient(VisitType visitType, LocalDateTime stopTime, LocalDateTime startTime, Patient patient);

    Optional<Visit> findByPatientAndStartTime(Patient patient, LocalDateTime localDateTime);


    List<Visit> findByPatientAndStatus(Patient patient, Status status);
    List<Visit> findByPatientAndStatusOrStatus(Patient patient, Status status, Status status1);
    List<Visit> findByEmployeeAndStatusAndStartTimeAfterAndStartTimeBefore(Employee employee, Status status, LocalDateTime stopTime, LocalDateTime startTime);
}
