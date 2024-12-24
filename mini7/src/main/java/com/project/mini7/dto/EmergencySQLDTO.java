package com.project.mini7.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

public class EmergencySQLDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmergencyDto {
        private int emClass;
        private Date callDate;
        private String text;
        private float latitude;
        private float longitude;

        private List<HospitalDto> HospitalList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HospitalDto {
        private String name;
        private String address;
        private String phone;
        private double latitude;
        private double longitude;
        private int moveTime;
    }
}
