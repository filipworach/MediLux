package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Short> {
    Optional<Status> findStatusByName(String name);

}
