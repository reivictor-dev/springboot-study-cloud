package com.study.study_spring.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.study.study_spring.data.dto.v2.PersonDTOV2;
import com.study.study_spring.model.Person;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person){
        PersonDTOV2 dto = new PersonDTOV2();

        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setSecondName(person.getSecondName());
        dto.setBirthDay(new Date());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        return dto;
    }

    public Person convertDTOToEntity(PersonDTOV2 personDTO){
        Person person = new Person();

        person.setId(personDTO.getId());
        person.setFirstName(personDTO.getFirstName());
        person.setSecondName(personDTO.getSecondName());
        //person.setBirthDay(new Date());
        person.setAddress(personDTO.getAddress());
        person.setGender(personDTO.getGender());
        return person;
    }
}
