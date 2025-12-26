package com.study.study_spring.integrationstests.books.dto;

import java.io.Serializable;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String author;

    private Date launchDate;

    private float price;

    private String title;

    public BookDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    
}
