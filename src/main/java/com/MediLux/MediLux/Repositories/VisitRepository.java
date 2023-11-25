package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientEmail(String email);
}