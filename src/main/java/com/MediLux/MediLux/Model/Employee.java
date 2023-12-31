package com.MediLux.MediLux.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "employees")
@Entity
public class Employee {
    @Id @GeneratedValue
    private long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String pesel;
    private String email;
    @Nullable
    private String streetName;
    private String houseNumber;
    @Nullable
    private String flatNumber;
    private String postCode;
    private String city;
    private boolean isDoctor;
    @OneToMany(mappedBy = "employee")
    private Set<Visit> visitList;
}
