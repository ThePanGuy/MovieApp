package com.myprojects.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemHealthService {
    private static final Logger log = LoggerFactory.getLogger(SystemHealthService.class);

    @Scheduled(fixedDelay = 60000)
    public void printSystemInfo() {
        log.info("Current server is alive.");
    }
}
