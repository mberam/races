package com.casumo.races.service;

import com.casumo.races.db.RaceParticipation;
import com.casumo.races.dto.DogRequestDto;
import com.casumo.races.db.Dog;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.BetRepository;
import com.casumo.races.repository.DogRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DogService {

    private final DogRepository dogRepository;
    private final RaceParticipationRepository raceParticipationRepository;
    private  final BetRepository betRepository;

    public List<Dog> findAllDogs() {
        return dogRepository.findAll();
    }

    public Dog saveDog(DogRequestDto dogRequestDto) {
        Dog dog = new Dog(null, dogRequestDto.name(), dogRequestDto.age(), dogRequestDto.breed(), null);
        return dogRepository.save(dog);
    }

    public Dog updateDog(Long id, DogRequestDto dogRequestDto) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new RacesException("Dog not found !", "Dog not found !", HttpStatus.NOT_FOUND));
        dog.setName(dogRequestDto.name());
        dog.setAge(dogRequestDto.age());
        dog.setBreed(dogRequestDto.breed());
        return dogRepository.save(dog);
    }

    public void deleteDog(Long id) {
        if (!dogRepository.existsById(id)) {
            throw new RacesException("Dog not found !", "Dog not found !", HttpStatus.NOT_FOUND);
        }

        List<RaceParticipation> participations = raceParticipationRepository.findByDogId(id);
        for (RaceParticipation participation : participations) {
            betRepository.deleteByRaceParticipationId(participation.getId());
            raceParticipationRepository.delete(participation);
        }
        dogRepository.deleteById(id);
    }

    public Dog findDogById(Long id) {
        return dogRepository.findById(id)
                .orElseThrow(() -> new RacesException("Dog not found !", "Dog not found !", HttpStatus.NOT_FOUND));
    }




}

