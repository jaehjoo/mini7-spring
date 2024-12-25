package com.project.mini7.controller;

import com.project.mini7.dto.MapDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page", "home");
        return "index";
    }

    @GetMapping("/as")
    public String info(Model model) {
        model.addAttribute("page", "as");
        return "as";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("page", "about");
        return "about";
    }
}
