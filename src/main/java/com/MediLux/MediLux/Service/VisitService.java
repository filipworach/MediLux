package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Dto.VisitDto;
import com.MediLux.MediLux.Exceptions.DoctorIsOccupiedException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Model.Status;
import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Repositories.EmployeeRepository;
import com.MediLux.MediLux.Repositories.PatientRepository;
import com.MediLux.MediLux.Repositories.StatusRepository;
import com.MediLux.MediLux.Repositories.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;
    private final StatusRepository statusRepository;
    public List<Visit> findByPatientEmail(String email) throws NotFoundException {
        return visitRepository.findByPatientEmail(email);
    }

    public Visit add(VisitDto visitDto) {
        Optional<Employee> doctor = employeeRepository.findById(visitDto.getEmployeeEmail());
        Optional<Patient> patient = patientRepository.findById(visitDto.getPatientEmail());
        Optional<Status> status = statusRepository.findStatusByName(visitDto.getStatusName());
        Visit visit = new Visit();
        if (patient.isPresent()) {
            visit.setPatient(patient.get());
        } else {
            throw new NotFoundException();
        }
        if (status.isPresent()) {
            visit.setStatus(status.get());
        } else {
            throw new NotFoundException();
        }
        if (doctor.isPresent()) {
            if (isDoctorAvailable(doctor.get(), visitDto.getStartTime(), visitDto.getStopTime())) {
                visit.setEmployee(doctor.get());
            } else {
                //throw new DoctorIsOccupiedException();
            }
        } else {
            throw new NotFoundException();
        }

        visit.setStartTime(visitDto.getStartTime());
        visit.setStopTime(visitDto.getStopTime());
        visit.setRoom(visitDto.getRoom());
        visit.setComment(visitDto.getComment());
        return visitRepository.save(visit);

    }

    private boolean isDoctorAvailable(Employee doctor, LocalDateTime startTime, LocalDateTime stopTime) {
        List<Visit> doctorVisits = visitRepository.findByEmployeeAndStopTimeAfterAndStartTimeBefore(doctor, startTime, stopTime);
        return doctorVisits.isEmpty();
    }
}
