package com.example.service1.controller;

import com.example.service1.feign.Service2Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Service1Controller {
    private final Service2Client service2Client;

    public Service1Controller(Service2Client service2Client) {
        this.service2Client = service2Client;
    }

    @GetMapping("/api/service1")
    public String getService1Data() {
        String service2Data = service2Client.getService2Data();
        return "Service1 data with: " + service2Data;
    }
}
