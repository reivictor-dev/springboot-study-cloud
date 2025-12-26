package com.study.study_spring.integrationstests.person.controller.cors.withxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.study.study_spring.config.TestConfigs;
import com.study.study_spring.integrationstests.person.dto.PersonDTOXml;
import com.study.study_spring.integrationstests.security.dto.AccountCredentialsDTO;
import com.study.study_spring.integrationstests.security.dto.TokenDTO;
import com.study.study_spring.integrationstests.testcontainers.AbstractIntegrationTest;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //como precisamos de uma ordem para que seja encaixado com as dependencias que serao inicializadas, precisamos deixar ordenado e nao aleatoria como é por padrao.
public class PersonControllerCorsWithXmlTest extends AbstractIntegrationTest{

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static PersonDTOXml person;
    private static final Logger logger = LoggerFactory.getLogger(PersonControllerCorsWithXmlTest.class);
    private static TokenDTO token;


    @BeforeAll //porque nao inicializamos fora do setUp? Pq o spring nao inicializa por norma. com isso nao estaria dentro de ciclo de vida do JUNIT
    static void setUp(){
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // como nao vamos verificar os Links que viram no JSON, geraria falha e acusaria nos testes, portanto sera utilizado isso para ignorar
        person = new PersonDTOXml();
    }

    @Test
    @Order(1)
    void signin(){
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        token = given()
            .basePath("/auth/signin")
             .port(TestConfigs.SERVER_PORT)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(credentials)
			.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.as(TokenDTO.class);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @Order(1) // como explicado antes, aqui setamos o que precisa vir primeiro, pois se o delete vem antes do create, o que ele vai deletar??
    void testCreate() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_CONTENT_TYPE, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_RIGHT_SITE)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .body(person)
			.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
        logger.info("XML recebido:\n{}", content);
        
        PersonDTOXml createdPerson = objectMapper.readValue(content, PersonDTOXml.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirst_name());
        assertNotNull(createdPerson.getSecond_name());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Victor",createdPerson.getFirst_name());
        assertEquals("King",createdPerson.getSecond_name());
        assertEquals("My Home - Paradise",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2) // como explicado antes, aqui setamos o que precisa vir primeiro, pois se o delete vem antes do create, o que ele vai deletar??
    void testCreateWithWrongOrigin() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_CONTENT_TYPE, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_WRONG_SITE)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .body(person)
			.when()
				.post()
			.then()
				.statusCode(403)
			.extract()
				.body()
					.asString();
        
        
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void testFindById() throws Exception, Exception  {
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_CONTENT_TYPE, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_RIGHT_SITE)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
            
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
        
        PersonDTOXml createdPerson = objectMapper.readValue(content, PersonDTOXml.class);
        person = createdPerson;

        assertEquals("Victor",createdPerson.getFirst_name());
        assertEquals("King",createdPerson.getSecond_name());
        assertEquals("My Home - Paradise",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }
    @Test
    @Order(4)
    void testFindByIdWithWrongSite() throws JsonMappingException, JsonProcessingException {
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_CONTENT_TYPE, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.APPLICATION_XML)
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_WRONG_SITE)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(403)
			.extract()
				.body()
					.asString();
        
        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson(){
        person.setFirst_name("Victor");
        person.setSecond_name("King");
        person.setAddress("My Home - Paradise");
        person.setGender("Male");
        person.setEnabled(true);
    } 

    
}
