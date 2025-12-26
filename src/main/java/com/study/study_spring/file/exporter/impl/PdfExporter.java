package com.study.study_spring.file.exporter.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.file.exporter.contract.PersonExporter;
import com.study.study_spring.services.QRCodeService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class PdfExporter implements PersonExporter{
    private Logger logger = LoggerFactory.getLogger(PdfExporter.class.getName());

    @Autowired
    QRCodeService qrCodeService;

    @Override
    public Resource ExportPeople(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/study-spring-people-report.jasper");
        if(inputStream == null) {
            throw new RuntimeException("template file not found: /templates/study-spring-people-report.jasper");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("title", "People Report");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters ,dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    @Override
    public Resource ExportPerson(PersonDTO person) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/study-spring-person-report.jasper");
        if(mainTemplateStream == null) {
            throw new RuntimeException("template file not found: /templates/study-spring-person-report.jasper");
        }

        InputStream subreportTemplateStream = getClass().getResourceAsStream("/templates/books-subreport.jasper");
        if(subreportTemplateStream == null) {
            throw new RuntimeException("template file not found: /templates/books-subreport.jasper");
        }

        JasperReport mainReport = (JasperReport) JRLoader.loadObject(mainTemplateStream);
        JasperReport subReport = (JasperReport) JRLoader.loadObject(subreportTemplateStream);

        /* utilizado para reports quando rodado localmente:

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subreportTemplateStream);
        
        */

        ///TODO Generate

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singleton(person));

        InputStream qrCodeStream = qrCodeService.generateQRCode(person.getProfileUrl(), 200, 0xc8);
        BufferedImage qrImage = ImageIO.read(qrCodeStream);
/*
        if (person.getBookList() == null || person.getBookList().isEmpty()) {
        logger.warn("Book list is empty or null for person: {}", person.getName());
        } else {
        logger.info("Book list size: {}", person.getBookList().size());
        person.getBookList().forEach(book -> logger.info("Book: {}", book.getTitle()));
        }
*/

        JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(person.getBookList());

        //String path = getClass().getResource("/templates/books-subreport.jasper").getPath();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUB_REPORT_DATA_SOURCE", subReportDataSource);
        parameters.put("BOOK_SUB_REPORT", subReport);
        //parameters.put("SUB_REPORT_DIR", path);
        parameters.put("QR_CODE_IMG", qrImage);


        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters ,mainDataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}
