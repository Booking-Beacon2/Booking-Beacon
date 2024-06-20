package com.bteam.Booking_Beacon.domain.basic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class BasicController {
    @GetMapping("health")
    public String healthCheck() {
        return "I am healthy";
    }
}
