package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {


}
