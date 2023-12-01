package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.VisitDto;
import com.MediLux.MediLux.Dto.VisitSeriesDto;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/visit")
public class VisitController {
    private final VisitService visitService;
    @GetMapping("/{patientEmail}")
    public ResponseEntity<List<Visit>> getVisitsByPatientId(@PathVariable String patientEmail) {
        return new ResponseEntity<>(visitService.findByPatientEmail(patientEmail), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVisit(@RequestBody VisitDto visitDto) {
        try {
            return new ResponseEntity<>(visitService.add(visitDto), HttpStatus.CREATED);
        } catch (NotFoundException ex) {
            // Obsługa NotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono: " + ex.getMessage());
        }
    }

    @PostMapping("/add_series")
    public ResponseEntity<?> addVisitSeries(@RequestBody VisitSeriesDto visitSeriesDto) {
        try {
            return new ResponseEntity<>(visitService.addVisitSeries(visitSeriesDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Złe zapytanie");
        }
    }

    @PostMapping("/find_by_type")
    public ResponseEntity<List<Visit>> findVisitByTypeAndDateRange(@RequestBody VisitDto visitDto) {
        return new ResponseEntity<>(visitService.getVisitByType(visitDto), HttpStatus.OK);
    }

    @PutMapping("/make_appointment")
    public ResponseEntity<Visit> makeAnAppointment(@RequestBody VisitDto visitDto) {
        return new ResponseEntity<>(visitService.makeAppointment(visitDto), HttpStatus.OK);
    }

    @PutMapping("/confirm_visit/{id}")
    public ResponseEntity<Visit> confirmVisit(@PathVariable Long id) {
        return new ResponseEntity<>(visitService.confirmVisit(id), HttpStatus.OK);
    }

    @PutMapping("/reject_visit/{id}")
    public ResponseEntity<Visit> rejectVisit(@PathVariable Long id) {
        return new ResponseEntity<>(visitService.rejectVisit(id), HttpStatus.OK);
    }





}
