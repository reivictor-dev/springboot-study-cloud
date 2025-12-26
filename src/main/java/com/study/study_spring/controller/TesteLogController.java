package com.study.study_spring.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("/api/test/v1")
public class TesteLogController {

    private final Logger logger = LoggerFactory.getLogger(TesteLogController.class.getName());

    @GetMapping()
    public String testLog(){
        logger.debug("This an DEBUG Log");
        logger.info("This an INFO Log");
        logger.warn("This an WARN Log");
        logger.error("This an ERROR Log");

        return "Logs generated successfully!";
    }
}
