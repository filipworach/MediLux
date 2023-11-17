package com.MediLux.MediLux.Model;

import org.springframework.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visits")
@Entity
public class Visit {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    @Nullable
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Employee employee;
    private LocalDate date;
    private LocalDate startTime;
    private LocalDate stopTime;
    @Nullable
    private String comment;
    private String room;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
}
