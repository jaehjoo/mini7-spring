package com.project.mini7.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.mini7.dto.EmergencyDTO;
import com.project.mini7.repository.EmergencyRepository;
import com.project.mini7.transutil.TransDtoToEntity;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final EmergencyRepository emergencyRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.host.name:null}")
    private String fastApiHostname;

    @GetMapping("/order")
    public String getOrder(@ModelAttribute EmergencyDTO.callInfoDto emergencyDTO, Model model) {

        // fastApi로 전송하기 위한 url 파악
        String fastApiUrl;
        if (fastApiHostname.equals("null")) {
            fastApiUrl = "http://" + System.getenv("AI_HOSTNAME") + ":8000/api/order";
        } else {
            fastApiUrl = fastApiHostname + "/api/order";
        }

        // fastApi로 get 요청을 하기 위한 쿼리문 작성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fastApiUrl)
                .queryParam("input_txt",  emergencyDTO.getText())
                .queryParam("hospital_num", emergencyDTO.getHospitalNum())
                .queryParam("start_lat", emergencyDTO.getStartLat())
                .queryParam("start_lng", emergencyDTO.getStartLng());

        // fastapi에서 받아온 json 문을 임시 entity 형태로 만들어 보관
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

        // json 문을 파싱해서 db에 저장. 만약 원하는 내용물이 아니면 error view로 이동
        Map<String, Object> map = TransDtoToEntity.ApiOrder(emergencyRepository, emergencyDTO, response);

        // 문제 없이 성공하면 searchResult로 이동
        if (map.get("status").equals("success")) {
            model.addAttribute("result", map.get("result"));
            return "searchResult";
        }

        // 실패하면 error 페이지로 이동
        model.addAttribute("error", map.get("result"));
        return "error";
    }
}
