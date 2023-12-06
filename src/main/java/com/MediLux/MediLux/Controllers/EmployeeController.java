package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.EmployeeDto;
import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasAuthority('LEKARZ')")
    @GetMapping("")
    public ResponseEntity<List<Employee>> findAllDoctors() {
        return new ResponseEntity<>(employeeService.findALlDoctors(), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('LEKARZ')")
    @PostMapping("/register")
    public ResponseEntity<Employee> register(@RequestBody EmployeeDto employeeDto) {
        try {
            return new ResponseEntity<>(employeeService.registerEmployee(employeeDto), HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAuthority('LEKARZ')")
    @GetMapping("{email}")
    public ResponseEntity<Employee> findDoctorByEmail(@PathVariable String email) {
        return new ResponseEntity<>(employeeService.findByEmail(email), HttpStatus.OK);
    }
}
