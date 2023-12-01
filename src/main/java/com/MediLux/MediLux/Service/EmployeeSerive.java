package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Dto.EmployeeDto;
import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Repositories.EmployeeRepository;
import com.MediLux.MediLux.Repositories.RoleRepository;
import liquibase.pro.packaged.E;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeSerive {
    EmployeeRepository employeeRepository;
    RoleRepository roleRepository;

    public Employee registerEmployee(EmployeeDto employeeDto) throws AlreadyExistsException {
        if (employeeRepository.findById(employeeDto.getEmail()).isPresent()) {
            throw new AlreadyExistsException();
        }
        Employee employee = new Employee();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(employeeDto.getPassword());
        employee.setPassword(encodedPassword);
        employee.setRole(roleRepository.findByName(employeeDto.getRole()).orElseThrow(() -> new NotFoundException("Nie znaleziono roli " + employeeDto.getRole())));
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setPesel(employeeDto.getPesel());
        employee.setStreetName(employeeDto.getStreetName());
        employee.setHouseNumber(employeeDto.getHouseNumber());
        employee.setFlatNumber(employeeDto.getFlatNumber());
        employee.setPostCode(employeeDto.getPostCode());
        employee.setCity(employeeDto.getCity());
        employee.setDoctor(employeeDto.isDoctor());
        employeeRepository.save(employee);

        return employee;
    }

    public List<Employee> findALlDoctors() {
        return employeeRepository.findAllByRole(roleRepository.findByName("DOCTOR").orElseThrow(() -> new NotFoundException("Nie znaleziono roli DOCTOR")));
    }
}
