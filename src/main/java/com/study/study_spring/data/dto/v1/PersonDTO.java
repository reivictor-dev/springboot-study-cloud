package com.study.study_spring.data.dto.v1;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.study.study_spring.model.Book;

@Relation(collectionRelation="people")
@JsonPropertyOrder({
    "id", "address", "firstName", "secondName", "gender"
})
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable  {

 
    private static final long serialVersionUID = 1l;

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("second_name")
    private String secondName;

    private String address;

    //@JsonSerialize(using=GenderSerializer.class)
    private String gender;

    private Boolean enabled;
    
    private String profileUrl;

    private String photoUrl;

    @JsonIgnore
    private List<Book> bookList;

    public PersonDTO() {

    }

    @JsonIgnore
    public String getName(){
        return (firstName != null ? firstName : "") + (secondName != null ? " " + secondName : "");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((secondName == null) ? 0 : secondName.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        result = prime * result + ((profileUrl == null) ? 0 : profileUrl.hashCode());
        result = prime * result + ((photoUrl == null) ? 0 : photoUrl.hashCode());
        result = prime * result + ((bookList == null) ? 0 : bookList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonDTO other = (PersonDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (secondName == null) {
            if (other.secondName != null)
                return false;
        } else if (!secondName.equals(other.secondName))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (enabled == null) {
            if (other.enabled != null)
                return false;
        } else if (!enabled.equals(other.enabled))
            return false;
        if (profileUrl == null) {
            if (other.profileUrl != null)
                return false;
        } else if (!profileUrl.equals(other.profileUrl))
            return false;
        if (photoUrl == null) {
            if (other.photoUrl != null)
                return false;
        } else if (!photoUrl.equals(other.photoUrl))
            return false;
        if (bookList == null) {
            if (other.bookList != null)
                return false;
        } else if (!bookList.equals(other.bookList))
            return false;
        return true;
    }

    
}