package com.project.mini7.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmergencyInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private int emClass;
    private Date callDate;
    private String text;
    private float latitude;
    private float longitude;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="emer")
    @JsonManagedReference
    private List<HospitalInfo> hospitalInfoList;
}
