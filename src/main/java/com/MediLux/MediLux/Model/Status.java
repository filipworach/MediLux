package com.MediLux.MediLux.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "statues")
@Entity
public class Status {
    @Id
    @GeneratedValue
    private short id;
    private String name;
    @Nullable
    private String description;
}
