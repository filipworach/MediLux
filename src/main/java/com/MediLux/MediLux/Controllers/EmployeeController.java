package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.EmployeeDto;
import com.MediLux.MediLux.Exceptions.AlreadyExistsException;
import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Service.EmployeeSerive;
import liquibase.pro.packaged.R;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeSerive employeeSerive;

    @GetMapping("")
    public ResponseEntity<List<Employee>> findAllDoctors() {
        return new ResponseEntity<>(employeeSerive.findALlDoctors(), HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<Employee> register(@RequestBody EmployeeDto employeeDto) {
        try {
            return new ResponseEntity<>(employeeSerive.registerEmployee(employeeDto), HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}
