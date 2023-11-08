package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient findById(long id) throws NotFoundException {
        return patientRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Patient findByEmail(String email) throws NotFoundException {
        return patientRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient patient) throws AlreadyExistsException {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new AlreadyExistsException();
        }
        patientRepository.save(patient);
        return patient;
    }

}
