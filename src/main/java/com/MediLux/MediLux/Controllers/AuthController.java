package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.UserDto;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Repositories.PatientRepository;
import com.MediLux.MediLux.Repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private PatientRepository patientRepository;
    private RoleRepository roleRepository;
    //private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody UserDto registerDto) {
        if (patientRepository.findById(registerDto.getEmail()).isPresent()) {
            return new ResponseEntity<>("User with that email already exists", HttpStatus.BAD_REQUEST);
        }
        Patient patient = new Patient();
        patient.setEmail(registerDto.getEmail());
        //patient.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }


}
