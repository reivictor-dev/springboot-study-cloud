package com.study.study_spring.integrationstests.security.controller.withyml;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.study.study_spring.config.TestConfigs;
import com.study.study_spring.integrationstests.books.controller.withyml.mapper.YamlMapper;
import com.study.study_spring.integrationstests.security.dto.AccountCredentialsDTO;
import com.study.study_spring.integrationstests.security.dto.TokenDTO;
import com.study.study_spring.integrationstests.testcontainers.AbstractIntegrationTest;

import static io.restassured.RestAssured.given;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

/**
 *
 * @author reivi
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYmlTest extends AbstractIntegrationTest{

    private static TokenDTO token;
    private static YamlMapper mapper;

    @BeforeAll 
    static void setUp(){
        mapper = new YamlMapper();
        token = new TokenDTO();
    }
    
    @Test
    @Order(1)
    void signin() throws JsonMappingException, JsonProcessingException{
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");
        //String ymlCredentials = new YAMLMapper().writeValueAsString(credentials);
        var content = given()
            .config(
                RestAssuredConfig.config()
                    .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                    )
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
            .accept(MediaType.APPLICATION_YAML_VALUE)
            .body(credentials, mapper)
			.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
                .body()
                    .as(TokenDTO.class, mapper);

        token = content;
        
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    
    @Test
    @Order(2)
    void refreshToken() throws JsonMappingException, JsonProcessingException{
         var content = given()
            .config(
                RestAssuredConfig.config()
                    .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                    )
            .basePath("/auth/refresh")
            .port(TestConfigs.SERVER_PORT)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
            .accept(MediaType.APPLICATION_YAML_VALUE)
            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
            .pathParam("username", token.getUsername())
			.when()
				.put("{username}")
			.then()
				.statusCode(200)
			.extract()
                .body()
                    .as(TokenDTO.class, mapper);

        token = content;
        
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

}