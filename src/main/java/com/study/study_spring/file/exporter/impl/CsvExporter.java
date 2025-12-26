package com.study.study_spring.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.file.exporter.contract.PersonExporter;

@Component
public class CsvExporter implements PersonExporter{

    @Override
    public Resource ExportPeople(List<PersonDTO> people) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.Builder.create()
            .setHeader("ID", "First Name", "Second Name", "Address", "Gender", "Enabled")
            .setSkipHeaderRecord(false)
            .get();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)){
            for(PersonDTO person : people){
                csvPrinter.printRecord(
                    person.getId(),
                    person.getFirstName(),
                    person.getSecondName(),
                    person.getAddress(),
                    person.getGender(),
                    person.getEnabled()
                    );
            }
        }
        return new ByteArrayResource(outputStream.toByteArray());
    }

    @Override
    public Resource ExportPerson(PersonDTO person) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
