package com.casumo.races.service;

import com.casumo.races.db.Dog;
import com.casumo.races.dto.DogRequestDto;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogServiceUnitTest {

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private DogService dogService;

    private DogRequestDto validDogRequestDto;
    private Dog validDog;

    @BeforeEach
    void setUp() {
        validDogRequestDto = new DogRequestDto("Max", 5, "Bulldog");
        validDog = new Dog(1L, "Max", 5, "Bulldog", null);
    }

    @Test
    void findAllDogs_ShouldReturnListOfDogs() {
        when(dogRepository.findAll()).thenReturn(List.of(validDog));
        List<Dog> dogs = dogService.findAllDogs();
        assertFalse(dogs.isEmpty());
        assertEquals(1, dogs.size());
        assertEquals(validDog, dogs.get(0));
    }

    @Test
    void saveDog_ShouldReturnSavedDog() {
        when(dogRepository.save(any(Dog.class))).thenReturn(validDog);
        Dog savedDog = dogService.saveDog(validDogRequestDto);
        assertNotNull(savedDog);
        assertEquals(validDogRequestDto.name(), savedDog.getName());
    }

    @Test
    void updateDog_WhenDogFound_ShouldReturnUpdatedDog() {
        when(dogRepository.findById(anyLong())).thenReturn(Optional.of(validDog));
        when(dogRepository.save(any(Dog.class))).thenReturn(validDog);
        Dog updatedDog = dogService.updateDog(1L, validDogRequestDto);
        assertNotNull(updatedDog);
        assertEquals(validDogRequestDto.name(), updatedDog.getName());
    }

    @Test
    void updateDog_WhenDogNotFound_ShouldThrowException() {
        when(dogRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RacesException.class, () -> dogService.updateDog(1L, validDogRequestDto));
    }

    @Test
    void deleteDog_WhenDogNotFound_ShouldThrowException() {
        when(dogRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(RacesException.class, () -> dogService.deleteDog(1L));
    }

    @Test
    void findDogById_WhenDogFound_ShouldReturnDog() {
        when(dogRepository.findById(anyLong())).thenReturn(Optional.of(validDog));
        Dog foundDog = dogService.findDogById(1L);
        assertEquals(validDog, foundDog);
    }

    @Test
    void findDogById_WhenDogNotFound_ShouldThrowException() {
        when(dogRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RacesException.class, () -> dogService.findDogById(1L));
    }
}
