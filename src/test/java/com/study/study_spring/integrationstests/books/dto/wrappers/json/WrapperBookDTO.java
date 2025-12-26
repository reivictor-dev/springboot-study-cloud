package com.study.study_spring.integrationstests.books.dto.wrappers.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperBookDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private BookEmbeddedDTO embedded;

    public WrapperBookDTO() {
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public BookEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(BookEmbeddedDTO embedded) {
        this.embedded = embedded;
    }

}
