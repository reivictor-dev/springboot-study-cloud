package com.study.study_spring.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {

}
