package com.study.study_spring.file.importer.contract;

import java.io.InputStream;
import java.util.List;

import com.study.study_spring.data.dto.v1.PersonDTO;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;

}
