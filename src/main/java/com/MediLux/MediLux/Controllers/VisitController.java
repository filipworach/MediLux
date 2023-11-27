package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.VisitDto;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Service.VisitService;
import liquibase.pro.packaged.R;
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
            return new ResponseEntity<>(visitService.add(visitDto), HttpStatus.OK);
        } catch (NotFoundException ex) {
            // Obsługa NotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono: " + ex.getMessage());
        }
    }



}
