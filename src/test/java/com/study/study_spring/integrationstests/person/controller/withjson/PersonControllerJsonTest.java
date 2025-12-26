package com.study.study_spring.integrationstests.person.controller.withjson;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.study_spring.config.TestConfigs;
import com.study.study_spring.integrationstests.person.dto.PersonDTO;
import com.study.study_spring.integrationstests.person.dto.wrappers.json.WrapperPersonDTO;
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
public class PersonControllerJsonTest extends AbstractIntegrationTest{

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;
    private static TokenDTO token;


    @BeforeAll //porque nao inicializamos fora do setUp? Pq o spring nao inicializa por norma. com isso nao estaria dentro de ciclo de vida do JUNIT
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // como nao vamos verificar os Links que viram no JSON, geraria falha e acusaria nos testes, portanto sera utilizado isso para ignorar
        person = new PersonDTO();
        token = new TokenDTO();
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
    @Order(2) // como explicado antes, aqui setamos o que precisa vir primeiro, pois se o delete vem antes do create, o que ele vai deletar??
    void testCreate() throws JsonProcessingException {
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
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(person)
			.when()
				.post()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirstName());
        assertEquals("SO",createdPerson.getSecondName());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3) // como explicado antes, aqui setamos o que precisa vir primeiro, pois se o delete vem antes do create, o que ele vai deletar??
    void testUpdate() throws JsonProcessingException {

        person.setSecondName("Updated Test");

        // NAO E NECESSARIO PASSAR SEMPRE A SPECIFICATION APENAS NO TEST 01, imagina um programa em execucao, os headers seriam passados de forma automatica para cada rota.
        // specification = new RequestSpecBuilder()
        //     .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_RIGHT_SITE)
        //         .setBasePath("/api/person/v1")
        //         .setPort(TestConfigs.SERVER_PORT)
        //         .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        //         .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        //     .build();
        
        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(person)
			.when()
				.put()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirstName());
        assertEquals("Updated Test",createdPerson.getSecondName());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirstName());
        assertEquals("Updated Test",createdPerson.getSecondName());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void testDisabled() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", person.getId())
			.when()
				.patch("{id}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
			.extract()
				.body()
					.asString();
        
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linux",createdPerson.getFirstName());
        assertEquals("Updated Test",createdPerson.getSecondName());
        assertEquals("His House",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(6)
    void testDelete() throws JsonMappingException, JsonProcessingException {
        given(specification)
            .pathParam("id", person.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(204);

    }


    

    @Test
    @Order(7)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 3, "size", 12, "direction", "asc")
			.when()
				.get()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
			.extract()
				.body()
					.asString();
        
        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();
        
        PersonDTO personOne = people.get(0);
        assertNotNull(personOne.getId());

        assertEquals("Andee",personOne.getFirstName());
        assertEquals("Jest",personOne.getSecondName());
        assertEquals("14th Floor",personOne.getAddress());
        assertEquals("Female",personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personThree = people.get(4);
        assertNotNull(personThree.getId());

        assertEquals("Anette",personThree.getFirstName());
        assertEquals("Everson",personThree.getSecondName());
        assertEquals("PO Box 18095",personThree.getAddress());
        assertEquals("Female",personThree.getGender());
        assertTrue(personThree.getEnabled());

        PersonDTO personEight = people.get(5);
        assertNotNull(personEight.getId());

        assertEquals("Ann-marie",personEight.getFirstName());
        assertEquals("Fuidge",personEight.getSecondName());
        assertEquals("PO Box 26687",personEight.getAddress());
        assertEquals("Female",personEight.getGender());
        assertFalse(personEight.getEnabled());
    }

    @Test
    @Order(8)
    void testFindByName() throws JsonMappingException, JsonProcessingException {

        //{{baseUrl}}/api/book/v1/findPeopleByTitle/te?page=0&size=2&direction=asc
        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("firstName", "te")
            .queryParam("page", 0, "size", 12, "direction", "asc")
			.when()
				.get("findPeopleByName/{firstName}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
			.extract()
				.body()
					.asString();
        
         WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();
        
        PersonDTO personOne = people.get(0);
        assertNotNull(personOne.getId());

        assertEquals("Anette",personOne.getFirstName());
        assertEquals("Everson",personOne.getSecondName());
        assertEquals("PO Box 18095",personOne.getAddress());
        assertEquals("Female",personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personThree = people.get(4);
        assertNotNull(personThree.getId());

        assertEquals("Boote",personThree.getFirstName());
        assertEquals("Bengle",personThree.getSecondName());
        assertEquals("Room 1950",personThree.getAddress());
        assertEquals("Male",personThree.getGender());
        assertFalse(personThree.getEnabled());

        PersonDTO personEight = people.get(5);
        assertNotNull(personEight.getId());

        assertEquals("Chryste",personEight.getFirstName());
        assertEquals("Bridgstock",personEight.getSecondName());
        assertEquals("Apt 1774",personEight.getAddress());
        assertEquals("Female",personEight.getGender());
        assertTrue(personEight.getEnabled());
    }

    private void mockPerson(){
        person.setFirstName("Linux");
        person.setSecondName("SO");
        person.setAddress("His House");
        person.setGender("Male");
        person.setEnabled(true);
    } 
}
