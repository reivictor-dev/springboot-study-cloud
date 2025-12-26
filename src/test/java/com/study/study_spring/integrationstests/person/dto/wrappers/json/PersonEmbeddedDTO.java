package com.study.study_spring.integrationstests.person.dto.wrappers.json;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.study_spring.integrationstests.person.dto.PersonDTO;

public class PersonEmbeddedDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> people;

    public PersonEmbeddedDTO() {
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }
    

}
