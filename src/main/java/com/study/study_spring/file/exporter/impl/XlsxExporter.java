package com.study.study_spring.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.file.exporter.contract.PersonExporter;

@Component
public class XlsxExporter implements PersonExporter{

    @Override
    public Resource ExportPeople(List<PersonDTO> people) throws Exception {
        try(Workbook workbook = new XSSFWorkbook()){
            //Cria uma aba no exce;
            Sheet sheet = workbook.createSheet("People"); 

            //Cria uma linha do cabecalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "First Name", "Second Name", "Address", "Gender", "Enabled"};
            for(int i = 0; i < headers.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowIndex = 1;
            for(PersonDTO person : people){
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getFirstName());
                row.createCell(2).setCellValue(person.getSecondName());
                row.createCell(3).setCellValue(person.getAddress());
                row.createCell(4).setCellValue(person.getGender());
                row.createCell(5).setCellValue(person.getEnabled() != null && person.getEnabled() ? "yes" : "no");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    @Override
    public Resource ExportPerson(PersonDTO person) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
