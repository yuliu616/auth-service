package com.yu.controller;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CurrentTimeController {

    public Instant getCurrentTime(){
        return Instant.now();
    }

}
