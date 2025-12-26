package com.study.study_spring.unittests.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.exception.RequiredObjectIsNullException;
import com.study.study_spring.model.Person;
import com.study.study_spring.repository.PersonRepository;
import com.study.study_spring.services.PersonServices;
import com.study.study_spring.unittests.mapper.mocks.MockPerson;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {
    
    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp(){
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testFindById() {
        
        Person person = input.mockEntity(1);
        person.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(person));

        var result = service.findById(1l);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Address test1", result.getAddress());
        assertEquals("FirstName test1", result.getFirstName());
        assertEquals("SecondName test1", result.getSecondName());
        assertEquals("Female", result.getGender());
        assertEquals("Photo test1", result.getPhotoUrl());
        assertEquals("ProfileUrl test1", result.getProfileUrl());



    }

    @Test
    void testCreate() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1l);
         
        PersonDTO personDTO = input.mockDTO(1);
        PersonDTO persistedDTO = personDTO;
        persistedDTO.setId(1l);
        when(repository.save(any(Person.class))).thenReturn((persisted));

        var result = service.create(personDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Address test1", result.getAddress());
        assertEquals("FirstName test1", result.getFirstName());
        assertEquals("SecondName test1", result.getSecondName());
        assertEquals("Female", result.getGender());
        assertEquals("Photo test1", result.getPhotoUrl());
        assertEquals("ProfileUrl test1", result.getProfileUrl());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
        ()-> {
            service.create(null);
        });
        
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
    
    @Test
    void testUpdate() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1l);
         
        PersonDTO personDTO = input.mockDTO(1);

        when(repository.findById(1l)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn((persisted));

        var result = service.update(personDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Address test1", result.getAddress());
        assertEquals("FirstName test1", result.getFirstName());
        assertEquals("SecondName test1", result.getSecondName());
        assertEquals("Female", result.getGender());  
        assertEquals("Photo test1", result.getPhotoUrl());
        assertEquals("ProfileUrl test1", result.getProfileUrl());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
        ()-> {
            service.update(null);
        });
        
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void testDelete() {
        Person person = input.mockEntity(1);
        person.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(person));

        service.delete(1l);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: Still Under Development")
    void testFindAll() {
        List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);

        List<PersonDTO> people = new ArrayList<>();

        assertNotNull(people);
        assertEquals(14, people.size());

        //person 1
        var personOne = people.get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getId());
        assertNotNull(personOne.getLinks());

        assertNotNull(personOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(personOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(personOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(personOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(personOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Address test1", personOne.getAddress());
        assertEquals("FirstName test1", personOne.getFirstName());
        assertEquals("SecondName test1", personOne.getSecondName());
        assertEquals("Female", personOne.getGender());  
        assertEquals("Photo test1", personOne.getPhotoUrl());
        assertEquals("ProfileUrl test1", personOne.getProfileUrl());

        //person 4
        var personFour = people.get(4);

        assertNotNull(personFour);
        assertNotNull(personFour.getId());
        assertNotNull(personFour.getLinks());

        assertNotNull(personFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(personFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(personFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(personFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(personFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Address test4", personFour.getAddress());
        assertEquals("FirstName test4", personFour.getFirstName());
        assertEquals("SecondName test4", personFour.getSecondName());
        assertEquals("Male", personFour.getGender());
        assertEquals("Photo test1", personFour.getPhotoUrl());
        assertEquals("ProfileUrl test1", personFour.getProfileUrl());
        

        //person 7
        var personSeven = people.get(7);

        assertNotNull(personSeven);
        assertNotNull(personSeven.getId());
        assertNotNull(personSeven.getLinks());

        assertNotNull(personSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(personSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(personSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(personSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(personSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Address test7", personSeven.getAddress());
        assertEquals("FirstName test7", personSeven.getFirstName());
        assertEquals("SecondName test7", personSeven.getSecondName());
        assertEquals("Female", personSeven.getGender());
        assertEquals("Photo test1", personSeven.getPhotoUrl());
        assertEquals("ProfileUrl test1", personSeven.getProfileUrl());
    
    }
}
