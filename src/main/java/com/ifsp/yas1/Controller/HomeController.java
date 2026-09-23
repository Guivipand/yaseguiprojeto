package com.ifsp.yas1.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/sucessCad")
    public String sucessoCad() {
        return "sucessCad";
    }
}