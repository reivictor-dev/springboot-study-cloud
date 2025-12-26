package com.study.study_spring.docs;

import org.springframework.http.ResponseEntity;

import com.study.study_spring.data.dto.v1.security.AccountCredentialsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AuthControllerDocs {

    @Operation(summary= "Authenticate an User and returns a token",
    description="Authenticate an User and returns a token",
    responses={
        @ApiResponse(description="Success",responseCode="200",content= @Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)
    })
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(summary= "Refresh Token for authenticated User and returns a token",
    description="Sending new AccessToken using the as a parameter the RefreshToken and Username",
    responses={
        @ApiResponse(description="Success",responseCode="200",content= @Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)
    })
    ResponseEntity<?> refreshToken(
            String username,
            String refreshToken);

    @Operation(summary= "Creating an User with credentials",
    description="Creating an User with credentials #Don't send this to production!",
    responses={
        @ApiResponse(description="Success",responseCode="200",content= @Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)
    })
    AccountCredentialsDTO createUser(AccountCredentialsDTO user);

}