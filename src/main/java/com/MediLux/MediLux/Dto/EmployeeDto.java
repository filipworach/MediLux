package com.MediLux.MediLux.Dto;

import com.MediLux.MediLux.Model.Role;
import com.MediLux.MediLux.Model.Visit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {

    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String pesel;

    private String streetName;
    private String houseNumber;
    private String password;

    private String flatNumber;
    private String postCode;
    private String city;
    private boolean isDoctor;

    private String role;
}
