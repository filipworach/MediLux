package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.VisitDto;
import com.MediLux.MediLux.Dto.VisitSeriesDto;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Service.VisitService;
import liquibase.pro.packaged.P;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/visit")
@CrossOrigin
public class VisitController {
    private final VisitService visitService;
    @GetMapping("/{patientEmail}")
    public ResponseEntity<List<Visit>> getVisitsByPatientId(@PathVariable String patientEmail) {
        return new ResponseEntity<>(visitService.findByPatientEmail(patientEmail), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PACJENT')")

    @GetMapping("/past_visits/{patientEmail}")
    public ResponseEntity<List<Visit>> getPastVisits(@PathVariable String patientEmail) {
        return new ResponseEntity<>(visitService.findPastVisits(patientEmail), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PACJENT')")

    @GetMapping("/future_visits/{patientEmail}")
    public ResponseEntity<List<Visit>> getFutureVisits(@PathVariable String patientEmail) {
        return new ResponseEntity<>(visitService.findFutureVisit(patientEmail), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('LEKARZ')")
    @PostMapping("/add")
    public ResponseEntity<?> addVisit(@RequestBody VisitDto visitDto) {
        try {
            return new ResponseEntity<>(visitService.add(visitDto), HttpStatus.CREATED);
        } catch (NotFoundException ex) {
            // Obsługa NotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('LEKARZ')")

    @PostMapping("/add_series")
    public ResponseEntity<?> addVisitSeries(@RequestBody VisitSeriesDto visitSeriesDto) {
        try {
            return new ResponseEntity<>(visitService.addVisitSeries(visitSeriesDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Złe zapytanie");
        }
    }

    @PreAuthorize("hasAuthority('PACJENT')")
    @PostMapping("/find_by_type")
    public ResponseEntity<List<VisitDto>> findVisitByTypeAndDateRange(@RequestBody VisitDto visitDto) {
        return new ResponseEntity<>(visitService.getVisitByType(visitDto), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('PACJENT')")

    @PutMapping("/make_appointment")
    public ResponseEntity<Visit> makeAnAppointment(@RequestBody VisitDto visitDto) {
        return new ResponseEntity<>(visitService.makeAppointment(visitDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LEKARZ')")
    @PutMapping("/confirm_visit/{id}")
    public ResponseEntity<Visit> confirmVisit(@PathVariable Long id) {
        return new ResponseEntity<>(visitService.confirmVisit(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LEKARZ')")
    @PutMapping("/reject_visit/{id}")
    public ResponseEntity<Visit> rejectVisit(@PathVariable Long id) {
        return new ResponseEntity<>(visitService.rejectVisit(id), HttpStatus.OK);
    }



    @PreAuthorize("hasAuthority('PACJENT')")
    @GetMapping("/find_my_visits/{email}")
    public ResponseEntity<List<VisitDto>> findVisits(@PathVariable String email) {
        return new ResponseEntity<>(visitService.findAssignedVisitsToPatient(email), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('LEKARZ')")
    @GetMapping("/find_visits_to_confirm/{email}")
    public ResponseEntity<List<VisitDto>> findVisitsToConfirm(@PathVariable String email) {
        return new ResponseEntity<>(visitService.findAssignedVisitsToDoctor(email), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('LEKARZ')")
    @PutMapping("/finish/{id}")
    public ResponseEntity<Visit> finishVisit(@PathVariable Long id, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(visitService.finishVisit(id, multipartFile), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LEKARZ')")
    @GetMapping("/find/{id}")
    public ResponseEntity<Visit> findVisitById(@PathVariable Long id) {
        return new ResponseEntity<>(visitService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LEKARZ')")
    @GetMapping("/find/{email}/{date}")
    public ResponseEntity<Visit> findVisitToFinish(@PathVariable String email, @PathVariable String date) {
        return new ResponseEntity<>(visitService.findVisitToFinish(email, date), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LEKARZ')")
    @GetMapping("/find_employee_visits/{email}/{date}")
    public ResponseEntity<List<Visit>> findVisitsAssignedToEmployee(@PathVariable String email, @PathVariable String date) {
        return new ResponseEntity<>(visitService.findVisitAssignedToEmployee(email, date), HttpStatus.OK);
    }

}
