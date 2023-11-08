package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getUserById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(patientService.findById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Patient>> getAllUsers() {
        return new ResponseEntity<>(patientService.findAll(), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        try {
            return new ResponseEntity<>(patientService.addPatient(patient), HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }



}
