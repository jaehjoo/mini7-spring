package com.project.mini7.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HospitalInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private int moveTime;

    @ManyToOne
    @JoinColumn(name="emer_id")
    @JsonBackReference
    private EmergencyInfo emer;
}
