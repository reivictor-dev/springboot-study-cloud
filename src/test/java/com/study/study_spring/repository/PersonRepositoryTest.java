package com.study.study_spring.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.study.study_spring.integrationstests.testcontainers.AbstractIntegrationTest;
import com.study.study_spring.model.Person;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{

    @Autowired
    PersonRepository repository;
    private static Person person;

    @BeforeAll
    static void setUp(){
        person = new Person();
    }

    @Test
    @Order(1)
    void testFindPeopleByName() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));
        
        person = repository.findPeopleByName("vi", pageable).getContent().get(0);
        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Bevin", person.getFirstName());
        assertEquals("Eilers", person.getSecondName());
        assertEquals("14th Floor", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void testDisabledPerson() {
        
        Long id = person.getId();
        repository.disabledPerson(id);

        var result = repository.findById(id);
        person = result.get();
        
        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Bevin", person.getFirstName());
        assertEquals("Eilers", person.getSecondName());
        assertEquals("14th Floor", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }

}
