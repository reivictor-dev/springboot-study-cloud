package com.study.study_spring.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJWTAuthenticationException extends AuthenticationException {

    public InvalidJWTAuthenticationException(String msg) {
        super(msg);
    }


    
}
