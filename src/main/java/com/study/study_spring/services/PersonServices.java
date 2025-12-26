package com.study.study_spring.services;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.study_spring.controller.PersonController;
import com.study.study_spring.data.dto.v1.PersonDTO;
import com.study.study_spring.data.dto.v2.PersonDTOV2;
import com.study.study_spring.exception.BadRequestException;
import com.study.study_spring.exception.FileStorageException;
import com.study.study_spring.exception.RequiredObjectIsNullException;
import com.study.study_spring.exception.ResourceNotFoundException;
import com.study.study_spring.file.exporter.contract.PersonExporter;
import com.study.study_spring.file.exporter.factory.FileExporterFactory;
import com.study.study_spring.file.importer.contract.FileImporter;
import com.study.study_spring.file.importer.factory.FileImporterFactory;
import static com.study.study_spring.mapper.ObjectMapper.parseObject;
import com.study.study_spring.mapper.custom.PersonMapper;
import com.study.study_spring.model.Person;
import com.study.study_spring.repository.PersonRepository;

@Service
public class PersonServices {
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> linkAssembler;

    public PersonDTO findById(Long id){
        logger.info("Finding one Person!");
        Person findedPerson = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        var dto = parseObject(findedPerson, PersonDTO.class);
        addHateoasLinks(dto); //refactor... extract method
        return dto;
    }

    public Resource exportPerson(Long id, String acceptHeader) throws Exception{
        logger.info("Export data of one Person!");
        var findedPerson = repository.findById(id)
            .map(entityPerson -> parseObject(entityPerson, PersonDTO.class))
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        PersonExporter exporter = this.exporter.getExporter(acceptHeader);
        return exporter.ExportPerson(findedPerson);
    }

    public Resource exportPeoplePage(Pageable pageable, String acceptHeader){
        logger.info("Exporting a People page!");
        var people = repository.findAll(pageable)
            .map(person -> parseObject(person, PersonDTO.class))
            .getContent();
        
        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.ExportPeople(people);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding people!");

        var people = repository.findAll(pageable);
        return createPeopleLinks(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName,Pageable pageable) {
        logger.info("Finding people by name!");

        var people = repository.findPeopleByName(firstName, pageable);
        return createPeopleLinks(pageable, people);
    }

    public List<PersonDTO> massPeopleCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if(file.isEmpty()) throw new BadRequestException("Please set a valid file!");

        try(InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new BadRequestException("File name connot "));
            FileImporter importer = this.importer.getImporter(filename);
            
            List<Person> entities = importer.importFile(inputStream).stream()
                .map(dto -> repository.save(parseObject(dto, Person.class)))
                .toList();
            
            return entities
                .stream()
                .map((entity) -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
                }).toList();
        } catch(Exception e){
            throw new FileStorageException("Error processing the file!");
        }
    }
    
    public PersonDTO create(PersonDTO personDTO){
        if(personDTO == null){
            throw new RequiredObjectIsNullException();
        }

        logger.info("Create a person");
        
        Person savedPerson = parseObject(personDTO, Person.class);
        savedPerson.setEnabled(true);
        var dto = parseObject(repository.save(savedPerson), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTOV2 createV2(PersonDTOV2 PersonDTOV2){
        logger.info("Create a person V2");
        
        Person savedPerson = personMapper.convertDTOToEntity(PersonDTOV2);
        return personMapper.convertEntityToDTO(repository.save(savedPerson));
    }

    public PersonDTO update(PersonDTO person){
        if(person == null){
            throw new RequiredObjectIsNullException();
        }
        logger.info("Update a person");

        Person findedPerson = repository.findById(person.getId())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        findedPerson.setFirstName(person.getFirstName());
        findedPerson.setSecondName(person.getSecondName());
        findedPerson.setAddress(person.getAddress());
        findedPerson.setGender(person.getGender());
        findedPerson.setEnabled(person.getEnabled());
        findedPerson.setProfileUrl(person.getProfileUrl());
        findedPerson.setPhotoUrl(person.getPhotoUrl());
        var dto = parseObject(repository.save(findedPerson), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO disabledPerson(Long id) {
        logger.info("Disabling a person");

        repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        repository.disabledPerson(id);
        
        var dto = parseObject(repository.findById(id).get(), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Delete a person");

        Person entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
       
        repository.delete(entity);
    }
    
    private PagedModel<EntityModel<PersonDTO>> createPeopleLinks(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map((Person person) -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(PersonController.class)
            .findAll(
                pageable.getPageNumber(), 
                pageable.getPageSize(), 
                String.valueOf(pageable.getSort())))
                .withSelfRel();
        return linkAssembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1,12,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("",1,12,"asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("massCreationPeople").withRel("massCreationPeople").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disabledPerson(dto.getId())).withRel("disabled").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));

        dto.add(linkTo(methodOn(PersonController.class).exportPeoplePage(
                1,12,"asc", null))
            .withRel("exportPeoplePage")
            .withType("GET")
                .withTitle("Export people page file")
            );

    }
    
}
