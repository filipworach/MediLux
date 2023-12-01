package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitTypeRepository extends JpaRepository<VisitType, Long> {
    Optional<VisitType> findByType(String name);
}
