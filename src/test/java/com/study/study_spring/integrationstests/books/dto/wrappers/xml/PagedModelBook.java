package com.study.study_spring.integrationstests.books.dto.wrappers.xml;

import java.io.Serializable;
import java.util.List;

import com.study.study_spring.integrationstests.books.dto.BookDTO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name="content")
    private List<BookDTO> content;

    public PagedModelBook() {
    }

    public List<BookDTO> getContent() {
        return content;
    }

    public void setContent(List<BookDTO> content) {
        this.content = content;
    }

    
}
