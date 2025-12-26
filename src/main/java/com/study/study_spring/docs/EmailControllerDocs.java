package com.study.study_spring.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.study.study_spring.data.dto.v1.request.EmailRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface EmailControllerDocs {

    @Operation(
    summary="Send an e-mail",
    description="Sends an e-mail by providing datils, subject and body",
    tags={"Email"},
    responses={
        @ApiResponse(description="Success",responseCode="200",content= @Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(
    summary="Send an e-mail with attachment",
    description="Sends an e-mail with attachment by providing datils, subject and body",
    tags={"Email"},
    responses={
        @ApiResponse(description="Success",responseCode="200",content= @Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile);

}
