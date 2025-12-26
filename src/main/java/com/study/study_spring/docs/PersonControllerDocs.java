package com.study.study_spring.docs;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.file.exporter.MediaTypes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PersonControllerDocs {

    @Operation(
    summary="Find all people",
    description="All people",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                array=@ArraySchema(schema=@Schema(implementation=PersonDTO.class))
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
        @RequestParam(value= "page", defaultValue="0" ) Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
        
    );

    @Operation(
    summary="Export people page",
    description="Export people page with as file XLSX or CSV",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content={
                @Content(mediaType=MediaTypes.APPLICATION_XLSX_VALUE),
                @Content(mediaType=MediaTypes.APPLICATION_XLSX_VALUE)
        }),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<Resource> exportPeoplePage(
        @RequestParam(value= "page", defaultValue="0" ) Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction,
        HttpServletRequest request
    );

    @Operation(
    summary="Massive people creation",
    description="Massive people creation with upload of XLSX or CSV",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                array=@ArraySchema(schema=@Schema(implementation=PersonDTO.class))
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    List<PersonDTO> massCreationPeople(MultipartFile file);

    @Operation(
    summary="Find people by name",
    description="Find people by name",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                array=@ArraySchema(schema=@Schema(implementation=PersonDTO.class))
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
        @PathVariable(value="firstName") String firstName,
        @RequestParam(value= "page", defaultValue="0" ) Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
        
    );

    @Operation(
    summary="Export a person as PDF",
    description="Export a person by ID as PDF",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content= @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    ResponseEntity<Resource> export(@PathVariable("id") Long id, HttpServletRequest request) throws Exception;

    @Operation(
    summary="Find a person",
    description="Find a person by ID",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=PersonDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    PersonDTO findById(Long id);

    @Operation(
    summary="Create a person",
    description="Create a person",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=PersonDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    PersonDTO create(PersonDTO person);

    @Operation(
    summary="Update a person",
    description="Update a person by ID",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=PersonDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    PersonDTO update(PersonDTO person);

        @Operation(
    summary="Delete a person",
    description="Delete a person by ID",
    tags={"People"},
    responses={
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)
    }
    )
    ResponseEntity<?> delete(Long id);

    @Operation(
    summary="Disabling a person",
    description="Disabling a person by ID",
    tags={"People"},
    responses={
        @ApiResponse(
            description="Success",
            responseCode="200",
            content=@Content(
                mediaType=MediaType.APPLICATION_JSON_VALUE,
                schema=@Schema(implementation=PersonDTO.class)
            )),
        @ApiResponse(description="No Content",responseCode="204",content=@Content),
        @ApiResponse(description="Bad request",responseCode="400",content=@Content),
        @ApiResponse(description="Unauthorized",responseCode="401",content=@Content),
        @ApiResponse(description="Not found",responseCode="404",content=@Content),
        @ApiResponse(description="Internal server error",responseCode="500",content=@Content)

    }
    )
    PersonDTO disabledPerson(Long id);
}