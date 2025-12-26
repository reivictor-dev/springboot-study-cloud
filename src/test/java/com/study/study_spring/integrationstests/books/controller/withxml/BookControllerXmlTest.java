package com.study.study_spring.integrationstests.books.controller.withxml;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
import com.study.study_spring.integrationstests.books.dto.BookDTO;
import com.study.study_spring.integrationstests.books.dto.wrappers.xml.PagedModelBook;
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
class BookControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static BookDTO book;
    private static TokenDTO token;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
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
    @Order(2)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_RIGHT_SITE)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
            .setBasePath("/api/book/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
                .body(book)
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
            .extract()
                .body()
                    .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Mello Mock",createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertEquals(25.0, createdBook.getPrice(), 0.0001);
        assertEquals("Title Mock",createdBook.getTitle());
    }

    @Test
    @Order(3) // como explicado antes, aqui setamos o que precisa vir primeiro, pois se o delete vem antes do create, o que ele vai deletar??
    void testUpdate() throws JsonProcessingException {

        book.setTitle("Updated Test");

        // NAO E NECESSARIO PASSAR SEMPRE A SPECIFICATION APENAS NO TEST 01, imagina um programa em execucao, os headers seriam passados de forma automatica para cada rota.
        // specification = new RequestSpecBuilder()
        //     .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_TEST_RIGHT_SITE)
        //         .setBasePath("/api/book/v1")
        //         .setPort(TestConfigs.SERVER_PORT)
        //         .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        //         .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        //     .build();
        
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .body(book)
			.when()
				.put()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();
        
        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Mello Mock",createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertEquals(25.0, createdBook.getPrice(), 0.0001);
        assertEquals("Updated Test",createdBook.getTitle());
    }

    @Test
    @Order(4)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("id", book.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();
        
        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Mello Mock",createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertEquals(25.0, createdBook.getPrice(), 0.0001);
        assertEquals("Updated Test",createdBook.getTitle());
    }

    
    @Test
    @Order(5)
    void testDelete() throws JsonMappingException, JsonProcessingException {
        given(specification)
            .pathParam("id", book.getId())
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
            .queryParam("page", 8, "size", 12, "direction", "asc")
			.when()
				.get()
			.then()
				.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
			.extract()
				.body()
					.asString();

        PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
        List<BookDTO> book = wrapper.getContent();

        BookDTO bookOne = book.get(0);
        assertNotNull(bookOne.getId());

        assertEquals("Ed Yourdon",bookOne.getAuthor());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals(70.87, bookOne.getPrice(), 0.0001);
        assertEquals("Structured Design: Fundamentals of a Discipline of Computer Program and Systems Design",bookOne.getTitle());

        BookDTO bookThree = book.get(1);
        assertNotNull(bookThree.getId());

        assertEquals("Ed Yourdon",bookThree.getAuthor());
        assertNotNull(bookThree.getLaunchDate());
        assertEquals(103.68, bookThree.getPrice(), 0.0001);
        assertEquals("Structured Design: Fundamentals of a Discipline of Computer Program and Systems Design",bookThree.getTitle());

        BookDTO bookEight = book.get(5);
        assertNotNull(bookEight.getId());

        assertEquals("Steven S. Skiena",bookEight.getAuthor());
        assertNotNull(bookEight.getLaunchDate());
        assertEquals(54.779998779296875, bookEight.getPrice(), 0.0001);
        assertEquals("The Algorithm Design Manual",bookEight.getTitle());
    }

    private void mockPerson(){
        book.setAuthor("Mello Mock");
        book.setLaunchDate(new Date());
        book.setPrice(25);
        book.setTitle("Title Mock");
    } 
}
