package com.example.service2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Service2Controller {
    @GetMapping("/api/service2")
    public String getService2Data() {
        return "Data from Service2";
    }
}