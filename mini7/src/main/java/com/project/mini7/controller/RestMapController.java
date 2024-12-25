package com.project.mini7.controller;

import com.project.mini7.dto.MapDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/map/rest")
@RequiredArgsConstructor
public class RestMapController {

    @Value("${naver.maps.cid:null}")
    private String clientId;

    @Value("${naver.maps.cpass:null}")
    private String clientSecret;

    @GetMapping("/dynamic")
    public ResponseEntity<String> getRouteDynamic(@ModelAttribute MapDTO.RouteDto route) {
        RestTemplate restTemplate = new RestTemplate();
        String directionUrl = UriComponentsBuilder.fromHttpUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
                .queryParam("start", route.getStartLng() + "," + route.getStartLat())
                .queryParam("goal", route.getEndLng() + "," + route.getEndLat())
                .build().toUriString();
        HttpHeaders headers = new HttpHeaders();

        if (clientId.equals("null")) {
            clientId = System.getenv("MAP_ID");
        }
        if (clientSecret.equals("null")) {
            clientSecret = System.getenv("MAP_SECRET");
        }
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        ResponseEntity<String> response = restTemplate.exchange(
                directionUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        return ResponseEntity.ok(response.getBody());
    }
}
