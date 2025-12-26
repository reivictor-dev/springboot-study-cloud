package com.study.study_spring.file.importer.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.study.study_spring.exception.BadRequestException;
import com.study.study_spring.file.importer.contract.FileImporter;
import com.study.study_spring.file.importer.impl.CsvImporter;
import com.study.study_spring.file.importer.impl.XlsxImporter;

@Component
public class FileImporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);
    
    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws Exception{
        if(fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
            //return new XlsxImporter();
        } else if(fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
            //return new CsvImporter();

        } else {
            throw new BadRequestException();
        }
    }
}
