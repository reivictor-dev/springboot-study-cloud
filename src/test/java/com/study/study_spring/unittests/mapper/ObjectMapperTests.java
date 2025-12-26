package com.study.study_spring.unittests.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.mapper.ObjectMapper;
import com.study.study_spring.model.Person;
import com.study.study_spring.unittests.mapper.mocks.MockPerson;

public class ObjectMapperTests {
    MockPerson inputObject;

    @BeforeEach
    public void setUp() {inputObject = new MockPerson();}

    @Test
    public void parseEntityToDTOTest(){
        PersonDTO output = ObjectMapper.parseObject(inputObject.mockEntity(), PersonDTO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("FirstName test0", output.getFirstName());
        assertEquals("SecondName test0", output.getSecondName());
        assertEquals("Address test0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseEntityToDTOListTest(){
        List<PersonDTO> outputList = ObjectMapper.parseListObject(inputObject.mockEntityList(), PersonDTO.class);
        PersonDTO outputZero = outputList.get(0);
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("FirstName test0", outputZero.getFirstName());
        assertEquals("SecondName test0", outputZero.getSecondName());
        assertEquals("Address test0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());


        PersonDTO outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("FirstName test7", outputSeven.getFirstName());
        assertEquals("SecondName test7", outputSeven.getSecondName());
        assertEquals("Address test7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());

        PersonDTO outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("FirstName test12", outputTwelve.getFirstName());
        assertEquals("SecondName test12", outputTwelve.getSecondName());
        assertEquals("Address test12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());

    }

    @Test
    public void parseDTOToEntityTest(){
        Person output = ObjectMapper.parseObject(inputObject.mockDTO(), Person.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("FirstName test0", output.getFirstName());
        assertEquals("SecondName test0", output.getSecondName());
        assertEquals("Address test0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseDTOToEntityListTest(){
        List<Person> outputList = ObjectMapper.parseListObject(inputObject.mockDTOList(), Person.class);
        Person outputZero = outputList.get(0);
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("FirstName test0", outputZero.getFirstName());
        assertEquals("SecondName test0", outputZero.getSecondName());
        assertEquals("Address test0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());


        Person outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("FirstName test7", outputSeven.getFirstName());
        assertEquals("SecondName test7", outputSeven.getSecondName());
        assertEquals("Address test7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());

        Person outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("FirstName test12", outputTwelve.getFirstName());
        assertEquals("SecondName test12", outputTwelve.getSecondName());
        assertEquals("Address test12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());

    }
}
