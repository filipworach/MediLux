package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.UserDto;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Exceptions.WrongCredentials;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Repositories.PatientRepository;
import com.MediLux.MediLux.Repositories.RoleRepository;
import com.MediLux.MediLux.Service.AuthService;
import com.MediLux.MediLux.Service.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;

//    @PostMapping("/signin")
//    public ResponseEntity<String> authenticateUser(@RequestBody UserDto registerDto) {
//        if (patientRepository.findById(registerDto.getEmail()).isPresent()) {
//            return new ResponseEntity<>("User with that email already exists", HttpStatus.BAD_REQUEST);
//        }
//        Patient patient = new Patient();
//        patient.setEmail(registerDto.getEmail());
//        //patient.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
//    }



    @PostMapping("/login")
    public ResponseEntity<UserDto> authenticateUser(@RequestBody UserDto userDto) {

        try {


            return new ResponseEntity<>(jwtService.generateToken(userDto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }  catch (WrongCredentials e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    }
    @PreAuthorize("hasAuthority('LEKARZ') or hasAuthority('PACJENT')")
    @PutMapping("/change_password")
    ResponseEntity<UserDto> changePassword(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(jwtService.changePassword(userDto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
