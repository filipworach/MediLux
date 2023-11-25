package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
