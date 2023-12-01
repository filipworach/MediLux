package com.MediLux.MediLux.Model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "employees")
@Entity
public class Employee {
    @Id
    @Column(name = "e_mail")
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String pesel;
    @Nullable
    private String streetName;
    private String houseNumber;
    @Nullable
    private String flatNumber;
    private String password;
    private String postCode;
    private String city;
    private boolean isDoctor;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
