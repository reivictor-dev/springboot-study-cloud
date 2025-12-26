package com.study.study_spring.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.study.study_spring.data.dto.v1.BookDTO;
import com.study.study_spring.model.Book;

public class MockBook {

    public Book mockEntity() {return mockEntity(0);};
    public BookDTO mockDTO() {return mockDTO(0);};

    public List<Book> mockEntityList(){
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }
    public List<BookDTO> mockDTOList(){
            List<BookDTO> books = new ArrayList<>();

            for (int i = 0; i < 14; i++) {
                books.add(mockDTO(i));
            }
            return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date());
        book.setPrice(number.floatValue());
        book.setTitle("Title Test" + number);
       
        return book;
        
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(number.longValue());
        bookDTO.setAuthor("Author Test" + number);
        bookDTO.setLaunchDate(new Date());
        bookDTO.setPrice(number.floatValue());
        bookDTO.setTitle("Title Test" + number);
       
        return bookDTO;
        
    }
}
