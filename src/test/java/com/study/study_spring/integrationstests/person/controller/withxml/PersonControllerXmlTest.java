package com.study.study_spring.integrationstests.person.controller.withxml;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import com.study.study_spring.integrationstests.person.dto.PersonDTOXml;
import com.study.study_spring.integrationstests.person.dto.wrappers.xml.PagedModelPerson;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static TokenDTO token;
    private static PersonDTOXml person;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

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
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
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
                .contentType(MediaType.APPLICATION_XML_VALUE)
            .extract()
                .body()
                    .asString();

        PersonDTOXml createdPerson = objectMapper.readValue(content, PersonDTOXml.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirst_name());
        assertEquals("SO",createdPerson.getSecond_name());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2) // como explicado antes, aqui setamos o que precisa vir primeiro, pois se o delete vem antes do create, o que ele vai deletar??
    void testUpdate() throws JsonProcessingException {

        person.setSecond_name("Updated Test");

        // NAO E NECESSARIO PASSAR SEMPRE A SPECIFICATION APENAS NO TEST 01, imagina um programa em execucao, os headers seriam passados de forma automatica para cada rota.
        // specification = new RequestSpecBuilder()
        //     .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_RIGHT_SITE)
        //         .setBasePath("/api/person/v1")
        //         .setPort(TestConfigs.SERVER_PORT)
        //         .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        //         .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        //     .build();
        
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .body(person)
			.when()
				.put()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTOXml createdPerson = objectMapper.readValue(content, PersonDTOXml.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirst_name());
        assertEquals("Updated Test",createdPerson.getSecond_name());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTOXml createdPerson = objectMapper.readValue(content, PersonDTOXml.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirst_name());
        assertEquals("Updated Test",createdPerson.getSecond_name());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testDisabled() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("id", person.getId())
			.when()
				.patch("{id}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTOXml createdPerson = objectMapper.readValue(content, PersonDTOXml.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirst_name());
        assertEquals("Updated Test",createdPerson.getSecond_name());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void testDelete() throws JsonMappingException, JsonProcessingException {
        given(specification)
            .pathParam("id", person.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(204);

    }


    

    @Test
    @Order(6)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .queryParam("page", 3, "size", 12, "direction", "asc")
			.when()
				.get()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        List<PersonDTOXml> people = wrapper.getContent();

        PersonDTOXml personOne = people.get(0);
        assertNotNull(personOne.getId());

        assertEquals("Andee",personOne.getFirst_name());
        assertEquals("Jest",personOne.getSecond_name());
        assertEquals("14th Floor",personOne.getAddress());
        assertEquals("Female",personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTOXml personThree = people.get(4);
        assertNotNull(personThree.getId());

        assertEquals("Anette",personThree.getFirst_name());
        assertEquals("Everson",personThree.getSecond_name());
        assertEquals("PO Box 18095",personThree.getAddress());
        assertEquals("Female",personThree.getGender());
        assertTrue(personThree.getEnabled());

        PersonDTOXml personEight = people.get(5);
        assertNotNull(personEight.getId());

        assertEquals("Ann-marie",personEight.getFirst_name());
        assertEquals("Fuidge",personEight.getSecond_name());
        assertEquals("PO Box 26687",personEight.getAddress());
        assertEquals("Female",personEight.getGender());
        assertFalse(personEight.getEnabled());
    }

    @Test
    @Order(7)
    void testFindByName() throws JsonMappingException, JsonProcessingException {

        //{{baseUrl}}/api/book/v1/findPeopleByTitle/te?page=0&size=2&direction=asc
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("firstName", "te")
            .queryParam("page", 0, "size", 12, "direction", "asc")
			.when()
				.get("findPeopleByName/{firstName}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        List<PersonDTOXml> people = wrapper.getContent();

        PersonDTOXml personOne = people.get(0);
        assertNotNull(personOne.getId());

        assertEquals("Anette",personOne.getFirst_name());
        assertEquals("Everson",personOne.getSecond_name());
        assertEquals("PO Box 18095",personOne.getAddress());
        assertEquals("Female",personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTOXml personThree = people.get(4);
        assertNotNull(personThree.getId());

        assertEquals("Boote",personThree.getFirst_name());
        assertEquals("Bengle",personThree.getSecond_name());
        assertEquals("Room 1950",personThree.getAddress());
        assertEquals("Male",personThree.getGender());
        assertFalse(personThree.getEnabled());

        PersonDTOXml personEight = people.get(5);
        assertNotNull(personEight.getId());

        assertEquals("Chryste",personEight.getFirst_name());
        assertEquals("Bridgstock",personEight.getSecond_name());
        assertEquals("Apt 1774",personEight.getAddress());
        assertEquals("Female",personEight.getGender());
        assertTrue(personEight.getEnabled());
    }


    private void mockPerson(){
        person.setFirst_name("Linux");
        person.setSecond_name("SO");
        person.setAddress("His House");
        person.setGender("Male");
        person.setEnabled(true);
    } 
}
