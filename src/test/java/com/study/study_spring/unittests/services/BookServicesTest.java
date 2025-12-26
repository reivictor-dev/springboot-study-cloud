package com.study.study_spring.unittests.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.study_spring.data.dto.v1.BookDTO;
import com.study.study_spring.exception.RequiredObjectIsNullException;
import com.study.study_spring.model.Book;
import com.study.study_spring.repository.BookRepository;
import com.study.study_spring.services.BookServices;
import com.study.study_spring.unittests.mapper.mocks.MockBook;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {
    
    MockBook input;

    @InjectMocks
    private BookServices service;

    @Mock
    BookRepository repository;

    Date dateTest = new Date();

    @BeforeEach
    void setUp(){
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testFindById() {
        
        Book book = input.mockEntity(1);
        book.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(book));

        var result = service.findById(1l);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(1.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());


    }

    @Test
    void testCreate() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1l);
         
        BookDTO bookDTO = input.mockDTO(1);
        BookDTO persistedDTO = bookDTO;
        persistedDTO.setId(1l);
        when(repository.save(any(Book.class))).thenReturn((persisted)); //esperava o objeto verdadeiro e nao o DTO por isso no teste lancou um erro.

        var result = service.create(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
        ()-> {
            service.create(null);
        });
        
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
    
    @Test
    void testUpdate() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1l);
         
        BookDTO bookDTO = input.mockDTO(1);

        when(repository.findById(1l)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn((persisted));

        var result = service.update(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
        assertEquals("Title Test1", result.getTitle());  
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
        ()-> {
            service.update(null);
        });
        
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void testDelete() {
        Book book = input.mockEntity(1);
        book.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(book));

        service.delete(1l);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: Still Under Development")
    void testFindAll() {
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> book = new ArrayList<>();

        assertNotNull(book);
        assertEquals(14, book.size());

        //book 1
        var bookOne = book.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        assertNotNull(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Author Test1", bookOne.getAuthor());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals(1, bookOne.getPrice());
        assertEquals("Title Test1", bookOne.getTitle());  

        //book 4
        var bookFour = book.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        assertNotNull(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Author Test4", bookFour.getAuthor());
        assertNotNull(bookFour.getLaunchDate());
        assertEquals(4, bookFour.getPrice());
        assertEquals("Title Test4", bookFour.getTitle());  

        //book 7
        var bookSeven = book.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        assertNotNull(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("GET")
            )
        );

        assertNotNull(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("POST")
            )
        );

        assertNotNull(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update") 
                && link.getHref().endsWith("/api/book/v1")
                && link.getType().equals("PUT")
            )
        );

        assertNotNull(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete") 
                && link.getHref().endsWith("/api/book/v1/1")
                && link.getType().equals("DELETE")
            )
        );
        
        assertEquals("Author Test7", bookSeven.getAuthor());
        assertNotNull(bookSeven.getLaunchDate());
        assertEquals(7, bookSeven.getPrice());
        assertEquals("Title Test7", bookSeven.getTitle());  

    
    }
}
