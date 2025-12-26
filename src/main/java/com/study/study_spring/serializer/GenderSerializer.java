package com.study.study_spring.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GenderSerializer extends JsonSerializer<String>{

    @Override
    public void serialize(String gender, JsonGenerator generator, SerializerProvider provider) throws IOException {
        String formatedGender = "Male".equals(gender) ? "M" : "F";
        generator.writeString(formatedGender);
    }
    
}
