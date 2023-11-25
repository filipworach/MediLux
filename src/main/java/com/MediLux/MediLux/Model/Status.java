package com.MediLux.MediLux.Model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "statuses")
@Entity
public class Status {
    @Id
    @GeneratedValue
    private short id;
    private String name;
    @Nullable
    private String description;
}
