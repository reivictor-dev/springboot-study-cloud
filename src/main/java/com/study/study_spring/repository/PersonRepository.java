package com.study.study_spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.study.study_spring.model.Person;

import jakarta.transaction.Transactional;

public interface PersonRepository extends JpaRepository<Person, Long>{


    @Modifying(clearAutomatically=true) //isso faz um clear ao CACHE do HIBERNATE, quando chamamos um atributo, por exemplo pelo ID, ele vem que esta no cache e nao o novo, isso fazia com o no findById tivessemos executando o disabled no cache e nao na entidade do banco. com esse clear, ja seta denovo com o novo atributo setado.
    @Transactional
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disabledPerson(@Param("id") Long id);

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT('%',:firstName, '%'))")    
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);

}
