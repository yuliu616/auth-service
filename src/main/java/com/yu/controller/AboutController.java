package com.yu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("${authService.apiBaseUrl}/about")
@RestController
public class AboutController {

    @Value("${authService.serviceName}")
    protected String serviceName;

    @Value("${authService.apiVersion}")
    protected String apiVersion;

    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

    @GetMapping("")
    public Map<String, Object> healthCheck(){
        logger.trace("healthCheck endpoint invoked");
        logger.debug("healthCheck endpoint invoked");
        logger.info("healthCheck endpoint invoked");
        logger.warn("healthCheck endpoint invoked");
        logger.error("healthCheck endpoint invoked");

        HashMap<String, Object> map = new HashMap<>();
        map.put("apiVersion", this.apiVersion);
        map.put("serviceName", this.serviceName);
        map.put("currentTime", java.time.Instant.now());
        map.put("currentDate", java.time.LocalDate.now());
        return map;
    }

}
