package com.casumo.races.controller;

import com.casumo.races.dto.DogDto;
import com.casumo.races.dto.DogRequestDto;
import com.casumo.races.db.Dog;
import com.casumo.races.mapper.DogMapper;
import com.casumo.races.service.DogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dogs")
public class DogController {
    private final DogService dogService;

    @GetMapping
    public ResponseEntity<List<DogDto>> getAllDogs() {
        List<Dog> dogs = dogService.findAllDogs();
        List<DogDto> dogDtos = DogMapper.getDogDtos(dogs);
        return ResponseEntity.ok(dogDtos);
    }

    @PostMapping
    public ResponseEntity<DogDto> addDog(@RequestBody DogRequestDto dogRequestDto) {
        Dog dog = dogService.saveDog(dogRequestDto);
        return ResponseEntity.ok(DogMapper.remapDog(dog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DogDto> updateDog(@PathVariable Long id, @RequestBody DogRequestDto dogRequestDto) {
        Dog updatedDog = dogService.updateDog(id, dogRequestDto);
        DogDto updatedDogDto = DogMapper.remapDog(updatedDog);
        return ResponseEntity.ok(updatedDogDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogDto> getDog(@PathVariable Long id) {
        Dog dog = dogService.findDogById(id);
        DogDto dogDto = DogMapper.remapDog(dog);
        return ResponseEntity.ok(dogDto);
    }
}
