package com.MediLux.MediLux.Repositories;

import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findAllByRole(Role role);
}
