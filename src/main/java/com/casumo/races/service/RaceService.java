package com.casumo.races.service;

import com.casumo.races.dto.RaceParticipationRequestDto;
import com.casumo.races.dto.RaceRequestDto;
import com.casumo.races.db.Dog;
import com.casumo.races.db.Race;
import com.casumo.races.db.RaceParticipation;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.BetRepository;
import com.casumo.races.repository.DogRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import com.casumo.races.repository.RaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RaceService {

    private final RaceRepository raceRepository;
    private final RaceParticipationRepository raceParticipationRepository;
    private final DogRepository dogRepository;
    private final BetRepository betRepository;

    public List<Race> findAllRaces() {
        return raceRepository.findAll();
    }

    public List<Dog> getDogsInRace(Long raceId) {

        List<RaceParticipation> participations = raceParticipationRepository.findByRaceId(raceId);
        return participations.stream().map(RaceParticipation::getDog).collect(Collectors.toList());
    }

    public Race addRace(RaceRequestDto raceRequestDto) {
        Race race = new Race(null, raceRequestDto.name(), raceRequestDto.raceTime(), null);
        return raceRepository.save(race);
    }

    public Race updateRace(Long raceId, RaceRequestDto raceRequestDto) {
        Race race = raceRepository.findById(raceId).orElseThrow(() ->
                new RacesException("Race not found!", "Race not found!", HttpStatus.NOT_FOUND));
        race.setName(raceRequestDto.name());
        race.setRaceTime(raceRequestDto.raceTime());
        return raceRepository.save(race);
    }

    public void deleteRace(Long raceId) {
        if (!raceRepository.existsById(raceId)) {
            throw new RacesException("Race not found!", "Race not found!", HttpStatus.NOT_FOUND);
        }
        List<RaceParticipation> participations = raceParticipationRepository.findByRaceId(raceId);
        for (RaceParticipation participation : participations) {
            betRepository.deleteByRaceParticipationId(participation.getId());
            raceParticipationRepository.delete(participation);
        }
        raceRepository.deleteById(raceId);
    }

    public Race getRace(Long raceId) {
        return raceRepository.findById(raceId).orElseThrow(() ->
                new RacesException("Race not found!", "Race not found!", HttpStatus.NOT_FOUND));
    }


    public RaceParticipation addRaceParticipation(RaceParticipationRequestDto participationDto) {
        Race race = raceRepository.findById(participationDto.raceId())
                .orElseThrow(() -> new RacesException("Race not found", "Race with ID " + participationDto.raceId() + " not found", HttpStatus.NOT_FOUND));

        Dog dog = dogRepository.findById(participationDto.dogId())
                .orElseThrow(() -> new RacesException("Dog not found", "Dog with ID " + participationDto.dogId() + " not found", HttpStatus.NOT_FOUND));

        RaceParticipation raceParticipation = new RaceParticipation();
        raceParticipation.setRace(race);
        raceParticipation.setDog(dog);
        raceParticipation.setOdds(participationDto.odds());
        return raceParticipationRepository.save(raceParticipation);
    }

    public RaceParticipation updateRaceParticipation(Long participationId, RaceParticipationRequestDto participationDto) {
        return raceParticipationRepository.findById(participationId).map(participation -> {
            Race race = raceRepository.findById(participationDto.raceId())
                    .orElseThrow(() -> new RacesException("Race not found", "Race with ID " + participationDto.raceId() + " not found", HttpStatus.NOT_FOUND));

            Dog dog = dogRepository.findById(participationDto.dogId())
                    .orElseThrow(() -> new RacesException("Dog not found", "Dog with ID " + participationDto.dogId() + " not found", HttpStatus.NOT_FOUND));

            participation.setRace(race);
            participation.setDog(dog);
            participation.setOdds(participationDto.odds());
            return raceParticipationRepository.save(participation);
        }).orElseThrow(() -> new RacesException("Race Participation not found!", "Race Participation not found!", HttpStatus.NOT_FOUND));
    }


    public void deleteRaceParticipation(Long participationId) {
        if (!raceParticipationRepository.existsById(participationId)) {
            throw new RacesException("Race Participation not found!", "Race Participation not found!", HttpStatus.NOT_FOUND);
        }
        raceParticipationRepository.deleteById(participationId);
    }

    public RaceParticipation getRaceParticipation(Long participationId) {
        return raceParticipationRepository.findById(participationId).orElseThrow(() -> new RacesException("Race Participation not found!", "Race Participation not found!", HttpStatus.NOT_FOUND));
    }




}
