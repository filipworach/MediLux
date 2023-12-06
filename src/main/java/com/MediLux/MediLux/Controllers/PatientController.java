package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.UserDto;
import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Service.PatientService;
import lombok.AllArgsConstructor;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "localhost:3000")

public class PatientController {
    private final PatientService patientService;

    @GetMapping("/{email}")
    public ResponseEntity<Patient> getUserById(@PathVariable String email) {
        try {
            return new ResponseEntity<>(patientService.findByEmail(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('PACJENT')")
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
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto)
    {
       return new ResponseEntity<>(patientService.login(userDto), HttpStatus.ACCEPTED);
    }


}
