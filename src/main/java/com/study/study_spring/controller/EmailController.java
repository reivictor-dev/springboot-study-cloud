package com.study.study_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.study_spring.data.dto.v1.request.EmailRequestDTO;
import com.study.study_spring.docs.EmailControllerDocs;
import com.study.study_spring.services.EmailService;


@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs{

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        emailService.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("Email sent with success!", HttpStatus.OK);
    }

    @Override
    @PostMapping(value="/withAttachment", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendEmailWithAttachment(
        @RequestParam("emailRequest") String emailRequest, 
        @RequestParam("attachment") MultipartFile attachment) {
        emailService.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("Email with attachment sent with success!", HttpStatus.OK);
    }

}
