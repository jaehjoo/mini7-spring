package com.project.mini7.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class EmergencyDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class callInfoDto {
        private String text;
        private int hospitalNum;
        float startLat;
        float startLng;
    }

    @Getter
    @Setter
    @Data
    public static class callResponseDto {
        @JsonProperty("em_class")
        private int emClass;

        @JsonProperty("hospital")
        private List<HospitalDto> hospitalList;

        @Data
        public static class HospitalDto {
            private int index;

            @JsonProperty("병원명")
            private String name;

            @JsonProperty("병원주소")
            private String address;

            @JsonProperty("위도")
            private double latitude;

            @JsonProperty("경도")
            private double longitude;

            @JsonProperty("병원등급")
            private String grade;

            @JsonProperty("등급 클래스명")
            private String className;

            @JsonProperty("기관ID")
            private String insId;

            @JsonProperty("전화번호")
            private String phone;

            @JsonProperty("소요시간(분)")
            private int moveTime;
        }

    }
}
