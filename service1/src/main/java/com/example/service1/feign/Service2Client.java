package com.example.service1.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service2", url = "${service2.url}")
public interface Service2Client {
    @GetMapping("/api/service2")
    String getService2Data();
}