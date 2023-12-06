package com.MediLux.MediLux.Model;

import org.springframework.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visits")
@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    @Nullable
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Employee employee;
    private LocalDateTime startTime;
    private LocalDateTime stopTime;
    @Nullable
    private String comment;
    private String room;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
    @ManyToOne
    @JoinColumn(name = "visit_type_id")
    private VisitType visitType;

    private byte[] prescription;


}
