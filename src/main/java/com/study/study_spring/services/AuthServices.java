package com.study.study_spring.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.study_spring.data.dto.v1.security.AccountCredentialsDTO;
import com.study.study_spring.data.dto.v1.security.TokenDTO;
import com.study.study_spring.exception.RequiredObjectIsNullException;
import com.study.study_spring.model.User;
import com.study.study_spring.repository.UserRepository;
import com.study.study_spring.security.jwt.JwtTokenProvider;

@Service
public class AuthServices {

    Logger logger = LoggerFactory.getLogger(AuthServices.class);
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials){
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentials.getUsername(), 
                credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername());
        
        if(user == null) throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");

        var token = tokenProvider.createAccessToken(
                credentials.getUsername(), 
                user.getRoles()
            );

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){
        var user = repository.findByUsername(username);
        TokenDTO token;
        if(user != null){
            token = tokenProvider.refreshToken(refreshToken);
        } else { 
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        
        
        return ResponseEntity.ok(token);
    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user){
        if(user == null){
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating a user");

        var entity = new User();
        entity.setUsername(user.getUsername());
        entity.setPassword(generatedHashedPassword(user.getPassword()));
        entity.setFullName(user.getFullname());
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        var dto = repository.save(entity);
        return new AccountCredentialsDTO(dto.getUsername(), dto.getPassword(), dto.getFullName());
    }

    private String generatedHashedPassword(String password) {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> enconders = new HashMap<>();
        enconders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", enconders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);
    }
}
