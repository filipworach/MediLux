package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findAllByRole(Role role);
    Optional<Employee> findByNameAndSurname(String name, String surname);
}
