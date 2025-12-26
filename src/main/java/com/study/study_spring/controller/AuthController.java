package com.study.study_spring.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.study_spring.data.dto.v1.security.AccountCredentialsDTO;
import com.study.study_spring.docs.AuthControllerDocs;
import com.study.study_spring.services.AuthServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {
    
    @Autowired
    AuthServices services;

    @Override
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials){
        if(credentials == null || StringUtils.isBlank(credentials.getPassword()) || StringUtils.isBlank(credentials.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = services.signIn(credentials);

        if(token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        return token;
    }

    @Override
    @PutMapping("/refresh/{username}")
    public ResponseEntity<?> refreshToken(
        @PathVariable("username") String username,
        @RequestHeader("Authorization") String refreshToken){
        if(parametersAreInvalid(username, refreshToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        System.out.println(refreshToken);
        var token = services.refreshToken(username, refreshToken);

        if(token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        return token;
    }

    @Override
    @PostMapping(value="/createUser",
    consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public AccountCredentialsDTO createUser(@RequestBody AccountCredentialsDTO user) {
        return services.create(user);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }
}
