package com.MediLux.MediLux.Model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
@Entity
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
