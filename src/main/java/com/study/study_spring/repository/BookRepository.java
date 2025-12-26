package com.study.study_spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.study.study_spring.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")    
    Page<Book> findBooksByAuthor(@Param("author") String author, Pageable pageable);
}
