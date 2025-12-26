package com.study.study_spring.file.exporter.contract;
import java.util.List;

import org.springframework.core.io.Resource;

import com.study.study_spring.data.dto.v1.PersonDTO;

public interface PersonExporter {

    Resource ExportPeople(List<PersonDTO> people) throws Exception;
    Resource ExportPerson(PersonDTO person) throws Exception;


}
