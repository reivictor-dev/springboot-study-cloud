package com.study.study_spring.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

@Service
public class ObjectMapper {

    //Mapeamento de DTO para entidade e vice versa
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();


    public static <O,D> D parseObject(O original, Class<D> destination){
        return mapper.map(original, destination);
    }

    public static <O,D> List<D> parseListObject(List<O> original, Class<D> destination){
        List <D> destinationObjects = new ArrayList<D>();
        for (Object o : original){
            destinationObjects.add(mapper.map(o, destination));
        }

        return destinationObjects;
    }
}
