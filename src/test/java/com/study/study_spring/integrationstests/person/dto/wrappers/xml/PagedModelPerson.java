package com.study.study_spring.integrationstests.person.dto.wrappers.xml;

import java.io.Serializable;
import java.util.List;

import com.study.study_spring.integrationstests.person.dto.PersonDTOXml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name="content")
    private List<PersonDTOXml> content;

    public PagedModelPerson() {
    }

    public List<PersonDTOXml> getContent() {
        return content;
    }

    public void setContent(List<PersonDTOXml> content) {
        this.content = content;
    }

    
}
