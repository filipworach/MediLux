package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Config.JwtKeyProvider;
import com.MediLux.MediLux.Dto.UserDto;
import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Exceptions.WrongCredentials;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Repositories.PatientRepository;
import com.MediLux.MediLux.Repositories.RoleRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;


    public Patient findByEmail(String email) throws NotFoundException {
        return patientRepository.findById(email).orElseThrow(NotFoundException::new);
    }


    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient patient) throws AlreadyExistsException {
        if (patientRepository.findById(patient.getEmail()).isPresent()) {
            throw new AlreadyExistsException();
        }
        patientRepository.save(patient);
        return patient;
    }

    public Patient registerPatient(Patient patient) throws AlreadyExistsException {
        if (patientRepository.findById(patient.getEmail()).isPresent()) {
            throw new AlreadyExistsException();
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(patient.getPassword());
        patient.setPassword(encodedPassword);
        patient.setRole(roleRepository.findByName("PATIENT").orElseThrow(() -> new NotFoundException("Nie znaleziono roli PATIENT")));
        patientRepository.save(patient);

        return patient;
    }

    public String login(UserDto userDto) throws NotFoundException {
        Optional<Patient> databasePatient = patientRepository.findById(userDto.getEmail());
        if (databasePatient.isEmpty()) {
            throw new NotFoundException();
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(userDto.getPassword(), databasePatient.get().getPassword())) {
            long currentTimeMillis = System.currentTimeMillis();
            return Jwts.builder()
                    .setSubject(userDto.getEmail())
                    .claim("roles", "user")
                    .setIssuedAt(new Date(currentTimeMillis))
                    .setExpiration(new Date(currentTimeMillis + 100000))
                    .signWith(JwtKeyProvider.secretKey)
                    .compact();
        } else {
            throw new WrongCredentials();
        }

    }
}
