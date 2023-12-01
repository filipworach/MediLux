package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Dto.VisitDto;
import com.MediLux.MediLux.Dto.VisitSeriesDto;
import com.MediLux.MediLux.Exceptions.DoctorIsOccupiedException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.*;
import com.MediLux.MediLux.Repositories.*;
import liquibase.pro.packaged.V;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;
    private final StatusRepository statusRepository;
    private final VisitTypeRepository visitTypeRepository;
    public List<Visit> findByPatientEmail(String email) throws NotFoundException {
        return visitRepository.findByPatientEmail(email);
    }

    public Visit add(VisitDto visitDto) {
        Optional<Employee> doctor = employeeRepository.findById(visitDto.getEmployeeEmail());
        Optional<Patient> patient = patientRepository.findById(visitDto.getPatientEmail());
        Optional<Status> status = statusRepository.findStatusByName(visitDto.getStatusName());
        Optional<VisitType> visitType = visitTypeRepository.findByType(visitDto.getVisitType());
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
        if (visitType.isPresent()) {
            visit.setVisitType(visitType.get());
        } else {
            throw new NotFoundException();
        }
        if (doctor.isPresent()) {
            if (isDoctorAvailable(doctor.get(), visitDto.getStartTime(), visitDto.getStopTime())) {
                visit.setEmployee(doctor.get());
            } else {
                throw new DoctorIsOccupiedException();
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

    public List<Visit> addVisitSeries(VisitSeriesDto visitSeriesDto) {
        List<Visit> addedVisits = new ArrayList<>();

        LocalDateTime startTime = visitSeriesDto.getStartTime();
        LocalDateTime stopTime = visitSeriesDto.getStopTime();
        short howLongBreakBetweenVisits = visitSeriesDto.getHowLongBreakBetweenVisits();
        short howLongVisit = visitSeriesDto.getHowLongVisit();
        Optional<VisitType> visitType = visitTypeRepository.findByType(visitSeriesDto.getVisitType());
        Optional<Employee> doctor = employeeRepository.findById(visitSeriesDto.getEmployeeEmail());
        Optional<Status> status = statusRepository.findStatusByName("NOWA");
        if (visitType.isEmpty() || doctor.isEmpty() || status.isEmpty()) {
            throw new NotFoundException();
        } else {
            if (isDoctorAvailable(doctor.get(), startTime, stopTime)) {
                while (startTime.isBefore(stopTime)) {
                    LocalDateTime visitStartTime = startTime;
                    LocalDateTime visitStopTime = startTime.plusMinutes(howLongVisit);
                    if (isDoctorAvailable(doctor.get(), visitStartTime, visitStopTime)) {
                        Visit newVisit = new Visit();
                        newVisit.setEmployee(doctor.get());
                        newVisit.setStartTime(visitStartTime);
                        newVisit.setStopTime(visitStopTime);
                        newVisit.setRoom(visitSeriesDto.getRoom());
                        newVisit.setStatus(status.get());
                        newVisit.setVisitType(visitType.get());
                        visitRepository.save(newVisit);
                        addedVisits.add(newVisit);
                    }

                    startTime = startTime.plusMinutes(howLongVisit + howLongBreakBetweenVisits);
                }
            }
        }

        return addedVisits;
    }

    //public Visit makeAnAppointment()

    public List<Visit> getVisitByType(VisitDto visitDto) {
        Optional<VisitType> visitType = visitTypeRepository.findByType(visitDto.getVisitType());
        List<Visit> visits = visitRepository.findByVisitTypeAndStopTimeAfterAndStartTimeBeforeAndPatient(
                visitType.get(),
                visitDto.getStartTime(),
                visitDto.getStartTime().plusDays(7),
                null
        );

        visits.sort(Comparator.comparing(Visit::getStartTime));

        return visits;
    }

    public Visit makeAppointment(VisitDto visitDto) {
        Optional<Visit> visit = visitRepository.findById(visitDto.getVisitId());
        Optional<Patient> patient = patientRepository.findById(visitDto.getPatientEmail());
        Optional<Status> status = statusRepository.findStatusByName("OCZEKUJĄCA NA POTWIERDZENIE");
        visit.get().setPatient(patient.get());
        visit.get().setStatus(status.get());
        return visitRepository.save(visit.get());
    }

    public Visit confirmVisit(Long id) {
        Optional<Visit> visit = visitRepository.findById(id);
        Optional<Status> status = statusRepository.findStatusByName("POTWIERDZONA");
        visit.get().setStatus(status.get());
        return visitRepository.save(visit.get());
    }

    public Visit rejectVisit(Long id) {
        Optional<Visit> visit = visitRepository.findById(id);
        Optional<Status> status = statusRepository.findStatusByName("NOWA");
        visit.get().setStatus(status.get());
        visit.get().setPatient(null);
        return visitRepository.save(visit.get());
    }

    private boolean isDoctorAvailable(Employee doctor, LocalDateTime startTime, LocalDateTime stopTime) {
        List<Visit> doctorVisits = visitRepository.findByEmployeeAndStopTimeAfterAndStartTimeBefore(doctor, startTime, stopTime);
        return doctorVisits.isEmpty();
    }
}
