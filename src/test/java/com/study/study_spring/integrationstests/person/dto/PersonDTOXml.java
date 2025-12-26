package com.study.study_spring.integrationstests.person.dto;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PersonDTOXml implements Serializable  {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String first_name;
    private String second_name;

    private String address;
    
    private String gender;

    private Boolean enabled;

       public PersonDTOXml() {
    }

       public static long getSerialversionuid() {
           return serialVersionUID;
       }

       public Long getId() {
           return id;
       }

       public void setId(Long id) {
           this.id = id;
       }

       public String getFirst_name() {
           return first_name;
       }

       public void setFirst_name(String first_name) {
           this.first_name = first_name;
       }

       public String getSecond_name() {
           return second_name;
       }

       public void setSecond_name(String second_name) {
           this.second_name = second_name;
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

       @Override
       public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((first_name == null) ? 0 : first_name.hashCode());
        result = prime * result + ((second_name == null) ? 0 : second_name.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        return result;
       }

       @Override
       public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonDTOXml other = (PersonDTOXml) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (first_name == null) {
            if (other.first_name != null)
                return false;
        } else if (!first_name.equals(other.first_name))
            return false;
        if (second_name == null) {
            if (other.second_name != null)
                return false;
        } else if (!second_name.equals(other.second_name))
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
        return true;
       }

       

    
}