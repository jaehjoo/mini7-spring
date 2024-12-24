package com.project.mini7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // about.html
    @GetMapping("/about")
    public String aboutPage() {
        return "about"; // templates/about.html 파일 반환
    }

    // as.html
    @GetMapping("/as")
    public String asPage() {
        return "as"; // templates/as.html 파일 반환
    }
}
