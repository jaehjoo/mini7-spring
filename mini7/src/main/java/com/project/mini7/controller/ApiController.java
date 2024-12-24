package com.project.mini7.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.mini7.dto.DashboardDTO;
import com.project.mini7.dto.EmergencyDTO;
import com.project.mini7.dto.EmergencySQLDTO;
import com.project.mini7.repository.EmergencyRepository;
import com.project.mini7.service.GetEmergencyDataService;
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
        model.addAttribute("result", map.get("result"));
        System.out.println(map.get("result"));
        return "error";
    }

    private final GetEmergencyDataService getEmergencyDataService;

    @GetMapping("/dashboard")
    public String getDashboardInfo(Model model) {
        // db의 data를 전부 불러온다
        List<EmergencySQLDTO.EmergencyDto> data = getEmergencyDataService.getAllEmergencyInfo();

        // dashboard에 전시할 내용물을 저장할 dto를 생성
        DashboardDTO.RecentCallRecordsDto callRecordsDto = new DashboardDTO.RecentCallRecordsDto();
        DashboardDTO.HospitalCallRankDto hospitalCallRankDto = new DashboardDTO.HospitalCallRankDto();
        DashboardDTO.RegionCallRankDto regionCallRankDto = new DashboardDTO.RegionCallRankDto();

        List<DashboardDTO.CallRecordDto> recordDtoList = new ArrayList<>();
        Map<String, Integer> regionMap = new HashMap<>();
        Map<String, Integer> hospitalMap = new HashMap<>();

        // EmergencyData를 역순으로 순회하면서 데이터를 저장한다
        int size = data.size();
        for (int i = size - 1; i >= 0; i--) {
            EmergencySQLDTO.EmergencyDto dto = data.get(i);

            if (size - i < 5) {
                DashboardDTO.CallRecordDto recordDto = new DashboardDTO.CallRecordDto();
                recordDto.setEmClass(dto.getEmClass());
                recordDto.setText(dto.getText());
                recordDto.setStartLat(dto.getLatitude());
                recordDto.setStartLng(dto.getLongitude());
                recordDtoList.add(recordDto);
            }
            for (EmergencySQLDTO.HospitalDto h : dto.getHospitalList()) {
                String addr = h.getAddress().split(" ")[0];
                String name = h.getName();
                regionMap.merge(addr, 1, Integer::sum);
                hospitalMap.merge(name, 1, Integer::sum);
            }
        }

        // region, hospital 순위 확인을 위해 정렬을 한다
        Map<String, Integer> sortedRegionMap = regionMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        Map<String, Integer> sortedHospitalMap = hospitalMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // 최종 결과물을 포장하기 위해 dto에 저장
        callRecordsDto.setCallRecords(recordDtoList);
        hospitalCallRankDto.setHospitalRanks(sortedHospitalMap);
        regionCallRankDto.setRegionRanks(sortedRegionMap);

        // dto를 model 속성으로 주입하여 dashboard 페이지에서 꺼낼 수 있도록 조치
        model.addAttribute("recentCall", callRecordsDto);
        model.addAttribute("hospitalRank", hospitalCallRankDto);
        model.addAttribute("regionRank", regionCallRankDto);

        // dashboard view 반환
        return "dashboard";
    }
}
