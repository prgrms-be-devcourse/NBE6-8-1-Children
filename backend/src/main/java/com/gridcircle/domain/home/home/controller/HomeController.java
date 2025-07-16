package com.gridcircle.domain.home.home.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("grid")
public class HomeController {

    @GetMapping("/main")
    public String mainPage() {
        return "Welcome to the GridCircle Home Page!";
    }
}
