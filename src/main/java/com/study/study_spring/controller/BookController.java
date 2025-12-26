package com.study.study_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.study_spring.data.dto.v1.BookDTO;
import com.study.study_spring.docs.BookControllerDocs;
import com.study.study_spring.services.BookServices;



@RequestMapping("api/book/v1")
@RestController
public class BookController implements BookControllerDocs{

    @Autowired
    BookServices services;
    
    @Override
    @GetMapping(produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
        @RequestParam(value="page", defaultValue="0") Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        return ResponseEntity.ok(services.findAllBooks(pageable));
    }

    @Override
    @GetMapping(value="/findBooksByAuthor/{author}",produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findBooksByTitle(
        @PathVariable(value="author") String author,
        @RequestParam(value="page", defaultValue="0") Integer page,
        @RequestParam(value= "size", defaultValue="12" ) Integer size,
        @RequestParam(value= "direction", defaultValue="asc" ) String direction
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        return ResponseEntity.ok(services.findBooksByAuthor(author, pageable));
    }

    @Override
    @GetMapping(value="/{id}" ,produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public BookDTO findById(@PathVariable Long id){
        return services.findById(id);
    }

    @Override
    @PostMapping(
        consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public BookDTO create(@RequestBody BookDTO book) {   
        return services.create(book);
    }

    @Override
    @PutMapping(
        consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    public BookDTO update(@RequestBody BookDTO book) {
        return services.update(book);
    }

    @Override
    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}
