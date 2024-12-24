package com.project.mini7.transutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mini7.dto.EmergencyDTO;
import com.project.mini7.dto.MessageDTO;
import com.project.mini7.entity.EmergencyInfo;
import com.project.mini7.entity.HospitalInfo;
import com.project.mini7.repository.EmergencyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransDtoToEntity {

    public static Map<String, Object> ApiOrder(EmergencyRepository emergencyRepository,
                                               EmergencyDTO.callInfoDto emergencyDTO,
                                               ResponseEntity<String> response) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            // response 본문 내용물을 파싱하여 responseDto에 넣는다
            EmergencyDTO.callResponseDto responseDto = mapper.readValue(response.getBody(), EmergencyDTO.callResponseDto.class);

            // emergencyDto에서 담은 내용물을 emergencyInfo entity에 삽입
            EmergencyInfo emergencyInfo = new EmergencyInfo();
            emergencyInfo.setCallDate(new Date());
            emergencyInfo.setText(emergencyDTO.getText());
            emergencyInfo.setLongitude(emergencyDTO.getStartLng());
            emergencyInfo.setLatitude(emergencyDTO.getStartLat());
            emergencyInfo.setEmClass(responseDto.getEmClass());

            // responseDto에 담은 내용물을 emergencyInfo entitiy에 삽입
            List<HospitalInfo> hospitalInfoList = new ArrayList<>();
            for (EmergencyDTO.callResponseDto.HospitalDto hospital : responseDto.getHospitalList()) {
                HospitalInfo hospitalInfo = new HospitalInfo();

                hospitalInfo.setName(hospital.getName());
                hospitalInfo.setAddress(hospital.getAddress());
                hospitalInfo.setPhone(hospital.getPhone());
                hospitalInfo.setLatitude(hospital.getLatitude());
                hospitalInfo.setLongitude(hospital.getLongitude());
                hospitalInfo.setMoveTime(hospital.getMoveTime());
                hospitalInfo.setEmer(emergencyInfo);
                hospitalInfoList.add(hospitalInfo);
            }
            emergencyInfo.setHospitalInfoList(hospitalInfoList);

            // emergencyInfo에 담은 내용물을 repository를 통해 영속화
            emergencyRepository.save(emergencyInfo);
            map.put("status", "success");
            map.put("result", responseDto);
        } catch (JsonProcessingException e) {
            map.put("status", "fail");
            try {
                MessageDTO.message messageDto = mapper.readValue(response.getBody(), MessageDTO.message.class);
                map.put("result", messageDto.getMessage());
            } catch (JsonProcessingException er) {
                // 입출력에서 오류가 발생한 경우, 별도 error 페이지로 안내하기 위한 flag
                map.put("result", "please check your input");
            }
        }
        return map;
    }
}
