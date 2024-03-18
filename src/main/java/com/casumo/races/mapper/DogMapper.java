package com.casumo.races.mapper;

import com.casumo.races.dto.DogDto;
import com.casumo.races.db.Dog;

import java.util.List;
import java.util.stream.Collectors;

public class DogMapper {

    public static DogDto remapDog(Dog dog) {
        if (dog == null) {
            return null;
        }
        return new DogDto(dog.getId(), dog.getName(), dog.getAge(), dog.getBreed());
    }

    public static List<DogDto> getDogDtos(List<Dog> dogs) {
        if (dogs.isEmpty()) {
            return null;
        }
        return dogs.stream()
                .map(DogMapper::remapDog)
                .collect(Collectors.toList());
    }

}