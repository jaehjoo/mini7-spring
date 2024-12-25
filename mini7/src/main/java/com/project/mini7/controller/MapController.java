package com.project.mini7.controller;

import com.project.mini7.dto.MapDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    @Value("${naver.maps.cid:null}")
    private String clientId;

    @Value("${naver.maps.cpass:null}")
    private String clientSecret;

    @GetMapping("/static")
    public String getRouteStatic(@ModelAttribute MapDTO.RouteDto route, Model model) {
        String staticMapUrl = UriComponentsBuilder.fromHttpUrl("https://naveropenapi.apigw.ntruss.com/map-static/v2/raster")
                .queryParam("w", 600)
                .queryParam("h", 400)
                .queryParam("mapType", "traffic")
                .queryParam("markers", "type:d|size:mid|color:Blue|pos:" + route.getStartLng() + URLEncoder.encode(" ", StandardCharsets.UTF_8) + route.getStartLat())
                .queryParam("markers", "type:d|size:mid|color:Red|pos:" + route.getEndLng() + URLEncoder.encode(" ", StandardCharsets.UTF_8) + route.getEndLat())
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

        System.out.println(staticMapUrl);
        ResponseEntity<byte[]> response = new RestTemplate().exchange(
                staticMapUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );

        byte[] imageBytes = response.getBody();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        model.addAttribute("mapImage", base64Image);

        return "mapStatic";
    }

    @GetMapping("/dynamic")
    public String start(@ModelAttribute MapDTO.RouteDto route, Model model) {
        if (clientId.equals("null")) {
            clientId = System.getenv("MAP_ID");
        }
        model.addAttribute("clientId", clientId);
        model.addAttribute("route", route);
        return "mapDynamic";
    }
}
