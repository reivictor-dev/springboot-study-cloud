package com.study.study_spring.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.study_spring.config.EmailConfig;
import com.study.study_spring.data.dto.v1.request.EmailRequestDTO;
import com.study.study_spring.mail.EmailSender;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;
   
    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDTO emailRequest){
        emailSender
            .to(emailRequest.getTo())
            .withSubject(emailRequest.getSubject())
            .withMessage(emailRequest.getBody())
            .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment){
        File tempFile = null;

        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);
            emailSender
                .to(emailRequestDTO.getTo())
                .withSubject(emailRequestDTO.getSubject())
                .withMessage(emailRequestDTO.getBody())
                .Attach(tempFile.getAbsolutePath())
                .send(emailConfig);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error parsing email request JSON!", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Error processing the attachment", ex);
        } finally {
            if(tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}
