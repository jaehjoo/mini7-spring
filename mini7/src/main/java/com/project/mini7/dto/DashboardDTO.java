package com.project.mini7.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class DashboardDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecentCallRecordsDto {
        private List<CallRecordDto> callRecords;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HospitalCallRankDto {
        private Map<String, Integer> hospitalRanks;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegionCallRankDto {
        private Map<String, Integer> regionRanks;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CallRecordDto {
        private String text;
        private int emClass;
        private int hospitalNum;
        float startLat;
        float startLng;
    }
}
