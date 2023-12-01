package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Model.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientEmail(String email);
    List<Visit> findByEmployeeAndStopTimeAfterAndStartTimeBefore(Employee doctor, LocalDateTime startTime, LocalDateTime stopTime);
    List<Visit> findByVisitTypeAndStopTimeAfterAndStartTimeBefore(VisitType visitType, LocalDateTime stopTime, LocalDateTime startTime);
    List<Visit> findByVisitTypeAndStopTimeAfterAndStartTimeBeforeAndEmployee(VisitType visitType, LocalDateTime stopTime, LocalDateTime startTime, Employee employee);

    List<Visit> findByVisitTypeAndStopTimeAfterAndStartTimeBeforeAndPatient(VisitType visitType, LocalDateTime stopTime, LocalDateTime startTime, Patient patient);

}
