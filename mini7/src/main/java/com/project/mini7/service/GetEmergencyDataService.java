package com.project.mini7.service;

import com.project.mini7.dto.EmergencySQLDTO;
import com.project.mini7.entity.EmergencyInfo;
import com.project.mini7.entity.HospitalInfo;
import com.project.mini7.repository.EmergencyRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetEmergencyDataService {
    private final EmergencyRepository emergencyRepository;

    public List<EmergencySQLDTO.EmergencyDto> getAllEmergencyInfo(){
        List<EmergencyInfo> infoList = emergencyRepository.findAll();
        return infoList.stream().map(this::convertSQLDB).collect(Collectors.toList());
    }

    private EmergencySQLDTO.EmergencyDto convertSQLDB(EmergencyInfo info){
        EmergencySQLDTO.EmergencyDto dto = new EmergencySQLDTO.EmergencyDto();
        List<EmergencySQLDTO.HospitalDto> hdto = new ArrayList<>();

        dto.setText(info.getText());
        dto.setCallDate(info.getCallDate());
        dto.setEmClass(info.getEmClass());
        dto.setLongitude(info.getLongitude());
        dto.setLatitude(info.getLatitude());

        for (HospitalInfo h : info.getHospitalInfoList()) {
            EmergencySQLDTO.HospitalDto new_h = new EmergencySQLDTO.HospitalDto();

            new_h.setName(h.getName());
            new_h.setLatitude(h.getLatitude());
            new_h.setLongitude(h.getLongitude());
            new_h.setPhone(h.getPhone());
            new_h.setAddress(h.getAddress());
            new_h.setMoveTime(h.getMoveTime());

            hdto.add(new_h);
        }
        dto.setHospitalList(hdto);

        return dto;
    }
}
