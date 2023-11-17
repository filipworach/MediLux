package com.MediLux.MediLux.Model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "patients")
@Entity
public class Patient {
    @GeneratedValue(generator = "patients_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "patients_seq", sequenceName = "patients_seq", allocationSize = 1)
    @Id
    private long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String pesel;
    private String streetName;
    private String houseNumber;
    private String flatNumber;
    private String postCode;
    private String city;
    @Column(name = "e_mail")
    private String email;
    private String password;
}
