package com.project.mini7.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MapDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDto {
        private double startLat;
        private double startLng;
        private double endLat;
        private double endLng;
    }
}
