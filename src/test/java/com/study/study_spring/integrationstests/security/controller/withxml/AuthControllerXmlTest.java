package com.study.study_spring.integrationstests.security.controller.withxml;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.study.study_spring.config.TestConfigs;
import com.study.study_spring.integrationstests.security.dto.AccountCredentialsDTO;
import com.study.study_spring.integrationstests.security.dto.TokenDTO;
import com.study.study_spring.integrationstests.testcontainers.AbstractIntegrationTest;

import static io.restassured.RestAssured.given;

/**
 *
 * @author reivi
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest{

    private static TokenDTO token;
    private static XmlMapper mapper;

    @BeforeAll 
    static void setUp(){
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        token = new TokenDTO();
    }
    
    @Test
    @Order(1)
    void signin() throws JsonMappingException, JsonProcessingException{
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        String content = given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .body(credentials)
			.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();

        token = mapper.readValue(content, TokenDTO.class);
        
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    
    @Test
    @Order(2)
    void refreshToken() throws JsonMappingException, JsonProcessingException{
         String content = given()
            .basePath("/auth/refresh")
            .port(TestConfigs.SERVER_PORT)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
            .pathParam("username", token.getUsername())
			.when()
				.put("{username}")
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();

        token = mapper.readValue(content, TokenDTO.class);
        
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

}