package com.study.study_spring.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";
    String HEADER_PARAM_CONTENT_TYPE = "Content-Type";
    String HEADER_PARAM_ACCEPT = "Accept";
    String APPLICATION_XML = "application/xml";
    String APPLICATION_YAML = "application/x-yaml";
    String ORIGIN_LOCALHOST = "http://localhost:8080";
    String ORIGIN_TEST_RIGHT_SITE = "http://localhost:3000";
    String ORIGIN_TEST_WRONG_SITE = "http://localtest:8080";
    


}
