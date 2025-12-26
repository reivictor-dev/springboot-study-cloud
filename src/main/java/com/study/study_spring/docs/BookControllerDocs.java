package com.study.study_spring.docs;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.study_spring.data.dto.v1.BookDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.websocket.server.PathParam;

public interface BookControllerDocs {

    @Operation(
    summary="Find all books",
    description="All books",
    tags={"Book"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                array=@ArraySchema(schema=@Schema(implementation=BookDTO.class))
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
        @RequestParam(value="page", defaultValue="0") Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
    );

    @Operation(
    summary="Find all books",
    description="All books",
    tags={"Book"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                array=@ArraySchema(schema=@Schema(implementation=BookDTO.class))
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<PagedModel<EntityModel<BookDTO>>> findBooksByTitle(
        @PathParam(value="title") String title,
        @RequestParam(value="page", defaultValue="0") Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
    );

    @Operation(
    summary="Find a Book",
    description="Find a book by ID",
    tags={"Book"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=BookDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    BookDTO findById(Long id);

    @Operation(
    summary="Create a book",
    description="Create a book",
    tags={"Book"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=BookDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)
    }
    )
    BookDTO create(BookDTO Book);

    @Operation(
    summary="Update a Book",
    description="Update a Book by ID",
    tags={"Book"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=BookDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    BookDTO update(BookDTO Book);

        @Operation(
    summary="Delete a Book",
    description="Delete a Book by ID",
    tags={"Book"},
    responses={
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)
    }
    )
    ResponseEntity<?> delete(Long id);

}