package com.study.study_spring.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.study.study_spring.controller.BookController;
import com.study.study_spring.data.dto.v1.BookDTO;
import com.study.study_spring.exception.RequiredObjectIsNullException;
import com.study.study_spring.exception.ResourceNotFoundException;
import com.study.study_spring.mapper.ObjectMapper;
import static com.study.study_spring.mapper.ObjectMapper.parseObject;
import com.study.study_spring.model.Book;
import com.study.study_spring.repository.BookRepository;

@Service
public class BookServices {
    
    @Autowired
    BookRepository repository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PagedResourcesAssembler<BookDTO> linkAssembler;

    Logger logger = LoggerFactory.getLogger(BookServices.class);


    public PagedModel<EntityModel<BookDTO>> findAllBooks(Pageable pageable){
        logger.info("Finding all books!");

        var books = repository.findAll(pageable);
        var booksWithLinks = books.map((Book book) -> {
            var dto = parseObject(book, BookDTO.class); 
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllBooksLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(BookController.class)
            .findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())))
                .withSelfRel();
        return linkAssembler.toModel(booksWithLinks, findAllBooksLink);
    }
    
    public PagedModel<EntityModel<BookDTO>> findBooksByAuthor(String author, Pageable pageable){
        logger.info("Finding books by title!");

        var books = repository.findBooksByAuthor(author, pageable);
        var booksWithLinks = books.map((Book book) -> {
            var dto = parseObject(book, BookDTO.class); 
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllBooksLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(BookController.class)
            .findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())))
                .withSelfRel();
        return linkAssembler.toModel(booksWithLinks, findAllBooksLink);
    }

    public BookDTO findById(Long id){
        logger.info("Finding a book using ID");

        var book = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("there is no Person to this ID!"));
        
        var dto = parseObject(book, BookDTO.class); 
        addHateoasLinks(dto);

        return dto;
    }



    public BookDTO create(BookDTO bookDTO){
        if(bookDTO == null){
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating a book");

        var createdBook = parseObject(bookDTO, Book.class);
        var dto = parseObject(repository.save(createdBook), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO bookDTO){
        if(bookDTO == null){
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating a book");


        Book book = repository.findById(bookDTO.getId())
            .orElseThrow(() -> new ResourceNotFoundException("there is no Person to this ID!"));

        book.setAuthor(bookDTO.getAuthor());
        book.setLaunchDate(bookDTO.getLaunchDate());
        book.setPrice(bookDTO.getPrice());
        book.setTitle(bookDTO.getTitle());

        var dto = parseObject(repository.save(book), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id){
                logger.info("Deleting a book");

        Book book = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("there is no Person to this ID!"));

        repository.delete(book);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findAll(12,1,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
