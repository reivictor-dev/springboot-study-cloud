package com.study.study_spring.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.model.Person;

public class MockPerson {

    public Person mockEntity() {return mockEntity(0);};
    public PersonDTO mockDTO() {return mockDTO(0);};

    public List<Person> mockEntityList(){
        List<Person> persons = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }
    public List<PersonDTO> mockDTOList(){
            List<PersonDTO> persons = new ArrayList<>();

            for (int i = 0; i < 14; i++) {
                persons.add(mockDTO(i));
            }
            return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Address test" + number);
        person.setFirstName("FirstName test" + number);
        person.setGender(((number % 2) == 0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setSecondName("SecondName test" + number);
        person.setPhotoUrl("Photo test" + number);
        person.setProfileUrl("ProfileUrl test" + number);
       
        return person;
        
    }

    public PersonDTO mockDTO(Integer number) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAddress("Address test" + number);
        personDTO.setFirstName("FirstName test" + number);
        personDTO.setGender(((number % 2) == 0) ? "Male" : "Female");
        personDTO.setId(number.longValue());
        personDTO.setSecondName("SecondName test" + number);
        personDTO.setPhotoUrl("Photo test" + number);
        personDTO.setProfileUrl("ProfileUrl test" + number);
       
        return personDTO;
        
    }
}
