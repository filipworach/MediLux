package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Config.JwtKeyProvider;
import com.MediLux.MediLux.Dto.UserDto;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Exceptions.WrongCredentials;
import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Repositories.EmployeeRepository;
import com.MediLux.MediLux.Repositories.PatientRepository;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
@Service
@AllArgsConstructor
public class AuthService {
    PatientRepository patientRepository;
    EmployeeRepository employeeRepository;
    public String login(UserDto userDto) throws NotFoundException {
        Optional<Patient> databasePatient = patientRepository.findById(userDto.getEmail());
        Optional<Employee> databaseEmployee = employeeRepository.findById(userDto.getEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (databasePatient.isPresent()) {
            if (bCryptPasswordEncoder.matches(userDto.getPassword(), databasePatient.get().getPassword())) {
                long currentTimeMillis = System.currentTimeMillis();
                return Jwts.builder()
                        .setSubject(userDto.getEmail())
                        .claim("roles", "patient")
                        .setIssuedAt(new Date(currentTimeMillis))
                        .setExpiration(new Date(currentTimeMillis + 100000))
                        .signWith(JwtKeyProvider.secretKey)
                        .compact();
            } else {
                throw new WrongCredentials();
            }
        } else if (databaseEmployee.isPresent()) {
            if (bCryptPasswordEncoder.matches(userDto.getPassword(), databasePatient.get().getPassword())) {
                long currentTimeMillis = System.currentTimeMillis();
                return Jwts.builder()
                        .setSubject(userDto.getEmail())
                        .claim("roles", "employee")
                        .setIssuedAt(new Date(currentTimeMillis))
                        .setExpiration(new Date(currentTimeMillis + 100000))
                        .signWith(JwtKeyProvider.secretKey)
                        .compact();
            } else {
                throw new WrongCredentials();
            }
        } else {
            throw new NotFoundException();
        }

    }

}
