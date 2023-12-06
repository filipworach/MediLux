package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Dto.VisitDto;
import com.MediLux.MediLux.Dto.VisitSeriesDto;
import com.MediLux.MediLux.Exceptions.DoctorIsOccupiedException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.*;
import com.MediLux.MediLux.Repositories.*;
import liquibase.pro.packaged.V;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        Optional<Status> status = statusRepository.findStatusByName("NOWA");
        Optional<VisitType> visitType = visitTypeRepository.findByType(visitDto.getVisitType());
        Visit visit = new Visit();
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
            if (isDoctorAvailable(doctor.get(), LocalDateTime.parse(visitDto.getStartTime().substring(0,23)), LocalDateTime.parse(visitDto.getStopTime().substring(0,23)))) {
                visit.setEmployee(doctor.get());
            } else {
                throw new DoctorIsOccupiedException();
            }
        } else {
            throw new NotFoundException();
        }

        visit.setStartTime(LocalDateTime.parse(visitDto.getStartTime().substring(0,23)));
        visit.setStopTime(LocalDateTime.parse(visitDto.getStopTime().substring(0,23)));
        visit.setRoom(visitDto.getRoom());
        visit.setComment(visitDto.getComment());
        return visitRepository.save(visit);

    }

    public List<Visit> addVisitSeries(VisitSeriesDto visitSeriesDto) {
        List<Visit> addedVisits = new ArrayList<>();

        LocalDateTime startTime = LocalDateTime.parse(visitSeriesDto.getStartTime().substring(0,23)).plusMinutes(60);
        LocalDateTime stopTime = LocalDateTime.parse(visitSeriesDto.getStopTime().substring(0,23)).plusMinutes(60);
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

    public List<VisitDto> getVisitByType(VisitDto visitDto) {
        Optional<VisitType> visitType = visitTypeRepository.findByType(visitDto.getVisitType());
        List<Visit> visits = visitRepository.findByVisitTypeAndStopTimeAfterAndStartTimeBeforeAndPatient(
                visitType.get(),
                LocalDateTime.parse(visitDto.getStartTime().substring(0,23)),
                LocalDateTime.parse(visitDto.getStartTime().substring(0,23)).plusDays(7),
                null
        );

        visits.sort(Comparator.comparing(Visit::getStartTime));
        List<VisitDto> visitDtos = getVisitDtos(visits);
        return visitDtos;
    }

    private static List<VisitDto> getVisitDtos(List<Visit> visits) {
        List<VisitDto> visitDtos = new ArrayList<>();
        for (Visit visit : visits) {
            VisitDto visitDto1 = new VisitDto();
            visitDto1.setVisitType(visit.getVisitType().getType());
            visitDto1.setId(visit.getId());
            visitDto1.setEmployeeName(visit.getEmployee().getName());
            visitDto1.setEmployeeSurname(visit.getEmployee().getSurname());
            visitDto1.setStartTime(visit.getStartTime().toString());
            visitDto1.setStopTime(visit.getStopTime().toString());
            visitDtos.add(visitDto1);
        }
        return visitDtos;
    }

    public Visit makeAppointment(VisitDto visitDto) {
        Optional<Visit> visit = visitRepository.findById(visitDto.getId());
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

    public List<VisitDto> findAssignedVisitsToPatient(String email) {
        List<Visit> visits = visitRepository.findByPatientEmail(email);
        List<VisitDto> visitDtos = new ArrayList<>();
        for(Visit visit : visits) {
            VisitDto visitDto = new VisitDto();
            visitDto.setVisitType(visit.getVisitType().getType());
            visitDto.setStartTime(visit.getStartTime().toString());
            visitDto.setStopTime(visit.getStopTime().toString());
            visitDto.setEmployeeName(visit.getEmployee().getName());
            visitDto.setEmployeeSurname(visit.getEmployee().getSurname());
            visitDtos.add(visitDto);
        }
        return visitDtos;
    }


    public List<VisitDto> findAssignedVisitsToDoctor(String email) {
        Optional<Employee> employee = employeeRepository.findById(email);
        Optional<Status> status = statusRepository.findStatusByName("OCZEKUJĄCA NA POTWIERDZENIE");
        List<Visit> visits = visitRepository.findByEmployeeAndStatus(employee.get(), status.get());
        List<VisitDto> visitDtos = new ArrayList<>();
        for(Visit visit : visits) {
            VisitDto visitDto = new VisitDto();
            visitDto.setId(visit.getId());
            visitDto.setVisitType(visit.getVisitType().getType());
            visitDto.setStartTime(visit.getStartTime().toString());
            visitDto.setStopTime(visit.getStopTime().toString());
            visitDto.setName(visit.getPatient().getName());
            visitDto.setSurname(visit.getPatient().getSurname());
            visitDtos.add(visitDto);
        }
        return visitDtos;
    }

    public Visit finishVisit(Long id, MultipartFile multipartFile) throws IOException {
        Optional<Visit> visit = visitRepository.findById(id);
        Optional<Status> status = statusRepository.findStatusByName("ZAKOŃCZONA");
        if (visit.isPresent() && status.isPresent()) {
            visit.get().setPrescription(multipartFile.getBytes());
            visit.get().setStatus(status.get());
        } else {
            throw new NotFoundException();
        }
        return visitRepository.save(visit.get());
    }

    public Visit findById(Long id) {
        Optional<Visit> visit = visitRepository.findById(id);
        return visit.get();

    }

    public Visit findVisitToFinish(String email, String date) {
        Optional<Patient> patient = patientRepository.findById(email);
        LocalDateTime localDateTime = LocalDateTime.parse(date.substring(0,19)).plusMinutes(60);
        Optional<Visit> visit = visitRepository.findByPatientAndStartTime(patient.get(), localDateTime);
        return visit.get();

    }

    public List<Visit> findPastVisits(String patientEmail) {
        Optional<Patient> patient = patientRepository.findById(patientEmail);
        Optional<Status> status = statusRepository.findStatusByName("ZAKOŃCZONA");
        return visitRepository.findByPatientAndStatus(patient.get(), status.get());
    }
    public List<Visit> findFutureVisit(String patientEmail) {
        Optional<Patient> patient = patientRepository.findById(patientEmail);
        Optional<Status> status = statusRepository.findStatusByName("POTWIERDZONA");
        Optional<Status> status1 = statusRepository.findStatusByName("OCZEKUJĄCA NA POTWIERDZENIE");

        return visitRepository.findByPatientAndStatusOrStatus(patient.get(), status.get(), status1.get());
    }

    public List<Visit> findVisitAssignedToEmployee(String email, String date) {
        Optional<Status> status = statusRepository.findStatusByName("POTWIERDZONA");
        Optional<Employee> employee = employeeRepository.findById(email);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsuj string do LocalDate
        LocalDateTime localDateTime = LocalDate.parse(date, formatter).plusDays(1).atStartOfDay();
        LocalDateTime localDateTime1 = localDateTime.plusDays(1);
        return visitRepository.findByEmployeeAndStatusAndStartTimeAfterAndStartTimeBefore(employee.get(), status.get(), localDateTime, localDateTime1);


    }
}
