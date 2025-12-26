package com.study.study_spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.data.dto.v2.PersonDTOV2;
import com.study.study_spring.docs.PersonControllerDocs;
import com.study.study_spring.file.exporter.MediaTypes;
import com.study.study_spring.services.PersonServices;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;



@RequestMapping("/api/person/v1")
@RestController
@Tag(name="People", description="Manage people division")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    @Override
    @GetMapping(produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
        @RequestParam(value= "page", defaultValue="0" ) Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(services.findAll(pageable));
    }
    
    @Override
    @GetMapping(value="/findPeopleByName/{firstName}",produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
        @PathVariable(value="firstName") String firstName,
        @RequestParam(value= "page", defaultValue="0" ) Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(services.findByName(firstName,pageable));
    }

    @Override
    @GetMapping(value="/{id}", produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PersonDTO findById(@PathVariable("id") Long id){
        return services.findById(id);
    }

    @Override
    @PostMapping(
    consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

    @Override
    @PostMapping(value="/massCreationPeople",
    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<PersonDTO> massCreationPeople(@RequestParam("file") MultipartFile file){
        return services.massPeopleCreation(file);
    }

    @Override
    @GetMapping(value="/exportPeoplePage",
    produces={
        MediaTypes.APPLICATION_CSV_VALUE,
        MediaTypes.APPLICATION_XLSX_VALUE,
        MediaTypes.APPLICATION_PDF_VALUE
    })
    public ResponseEntity<Resource> exportPeoplePage(
        @RequestParam(value= "page", defaultValue="0" ) Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction,
        HttpServletRequest request) {
            
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = services.exportPeoplePage(pageable, acceptHeader);

        Map<String, String> extensionMap = Map.of(
            MediaTypes.APPLICATION_XLSX_VALUE, ".xlsx",
            MediaTypes.APPLICATION_CSV_VALUE, ".csv",
            MediaTypes.APPLICATION_PDF_VALUE, ".pdf"
        );
        
        var fileExtension = extensionMap.getOrDefault(acceptHeader, "");

        var fallbackType = MediaType.APPLICATION_PDF_VALUE;
        var contentType = (acceptHeader != null && !acceptHeader.equals("*/*")) ? acceptHeader : fallbackType;
 
        var filename = "people_exported" + fileExtension;

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(file);
    }

    @GetMapping(value="/exportPersonPage/{id}",
    produces=MediaTypes.APPLICATION_PDF_VALUE)
    @Override
    public ResponseEntity<Resource> export(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = services.exportPerson(id, acceptHeader);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(acceptHeader))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=person.pdf")
            .body(file);
    }

    @PostMapping(
    value="/v2",
    consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PersonDTOV2 create(@RequestBody PersonDTOV2 person) {
        return services.createV2(person);
    }

    @Override
    @PutMapping(
    consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    
    public PersonDTO update(@RequestBody PersonDTO person) {
        return services.update(person);
    }

    @Override
    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
       services.delete(id);

       return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(value="/{id}", produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PersonDTO disabledPerson(@PathVariable("id")Long id) {
        return services.disabledPerson(id);
    }

    

    
}
