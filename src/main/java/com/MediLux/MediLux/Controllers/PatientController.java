package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Service.PatientService;
import lombok.AllArgsConstructor;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "localhost:3000/*", methods = RequestMethod.POST)

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

    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient)
    {
        try {
            return new ResponseEntity<>(patientService.registerPatient(patient), HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Patient patient)
    {
       return new ResponseEntity<>(patientService.login(patient), HttpStatus.ACCEPTED);
    }


}
