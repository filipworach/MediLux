package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Dto.UserDto;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Exceptions.WrongCredentials;
import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Model.Role;
import com.MediLux.MediLux.Repositories.EmployeeRepository;
import com.MediLux.MediLux.Repositories.PatientRepository;
import com.MediLux.MediLux.Repositories.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import liquibase.pro.packaged.E;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.MediLux.MediLux.Config.JwtKeyProvider.secretKey;
@AllArgsConstructor
@Service
public class JWTService {

  // Klucz prywatny używany do podpisywania JWT

    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
     // Czas wygaśnięcia tokena w milisekundach

    public UserDto generateToken(UserDto userDto) {
        Date now = new Date();
        long expiration =  3600000;
        Optional<Employee> employee = employeeRepository.findById(userDto.getEmail());
        Optional<Patient> patient = patientRepository.findById(userDto.getEmail());
        Optional<Role> role = null;

        if(employee.isEmpty() && patient.isEmpty()) {
            throw new NotFoundException();

        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(employee.isPresent()) {
            if(!bCryptPasswordEncoder.matches(userDto.getPassword(), employee.get().getPassword())) throw new WrongCredentials();
            role = roleRepository.findByName(employee.get().getRole().getName());
        } else {
            if(!bCryptPasswordEncoder.matches(userDto.getPassword(), patient.get().getPassword())) throw new WrongCredentials();
            role = roleRepository.findByName(patient.get().getRole().getName());
        }


        Date expiryDate = new Date(now.getTime() + expiration);
        userDto.setRole(role.get().getName());
        userDto.setToken(Jwts.builder()
                .setSubject(userDto.getEmail())
                .claim("roles", role.get().getName()) // Dodanie claim z rolą użytkownika
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact());
        userDto.setPassword("");
        return userDto;
    }



    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UserDto changePassword(UserDto userDto) {
        Optional<Employee> employee = employeeRepository.findById(userDto.getEmail());
        Optional<Patient> patient = patientRepository.findById(userDto.getEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (employee.isPresent()) {
            if (bCryptPasswordEncoder.matches(userDto.getPassword(), employee.get().getPassword()) ) {
                employee.get().setPassword(bCryptPasswordEncoder.encode(userDto.getNewPassword()));
                employeeRepository.save(employee.get());
                userDto.setPassword(" ");
                userDto.setNewPassword(" ");
                return userDto;
            }
        } else if (patient.isPresent()) {
            if (bCryptPasswordEncoder.matches(userDto.getPassword(), patient.get().getPassword()) ) {
                patient.get().setPassword(bCryptPasswordEncoder.encode(userDto.getNewPassword()));
                patientRepository.save(patient.get());
                userDto.setPassword(" ");
                userDto.setNewPassword(" ");
                return userDto;
            }
        } else throw new NotFoundException();
        userDto.setNewPassword(" ");
        return userDto;
    }
}
