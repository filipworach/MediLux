package com.MediLux.MediLux.Model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "patients")
@Entity
public class Patient {

    @Id
    @Column(name = "e_mail")
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String pesel;
    private String streetName;
    private String houseNumber;
    private String flatNumber;
    private String postCode;
    private String city;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
