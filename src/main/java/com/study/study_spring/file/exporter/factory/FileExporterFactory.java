package com.study.study_spring.file.exporter.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.study.study_spring.exception.BadRequestException;
import com.study.study_spring.file.exporter.MediaTypes;
import com.study.study_spring.file.exporter.contract.PersonExporter;
import com.study.study_spring.file.exporter.impl.CsvExporter;
import com.study.study_spring.file.exporter.impl.PdfExporter;
import com.study.study_spring.file.exporter.impl.XlsxExporter;

@Component
public class FileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);
    
    @Autowired
    private ApplicationContext context;

    public PersonExporter getExporter(String acceptHeader) throws Exception{
        if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            return context.getBean(PdfExporter.class);
        } else {
            throw new BadRequestException();
        }
    }
}
