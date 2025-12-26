package com.study.study_spring.integrationstests.swagger;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.study_spring.config.TestConfigs;
import com.study.study_spring.integrationstests.testcontainers.AbstractIntegrationTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void shouldDisplaySwaggerUIPage() {
		var content = given()
			.basePath("/swagger-ui/index.html")
				.port(TestConfigs.SERVER_PORT)
			.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
		assertTrue(content.contains("Swagger UI"));
	}
}