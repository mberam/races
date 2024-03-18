package com.casumo.races.controller;

import com.casumo.races.dto.*;
import com.casumo.races.db.Dog;
import com.casumo.races.db.Race;
import com.casumo.races.db.RaceParticipation;
import com.casumo.races.mapper.DogMapper;
import com.casumo.races.mapper.RaceMapper;
import com.casumo.races.mapper.RaceParticipationMapper;
import com.casumo.races.service.RaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/races")
public class RaceController {
    private final RaceService raceService;

    @GetMapping
    public ResponseEntity<List<RaceDto>> getAllRaces() {
        List<Race> races = raceService.findAllRaces();
        List<RaceDto> raceDtos = RaceMapper.getRaceDtos(races);
        return ResponseEntity.ok(raceDtos);
    }

    @GetMapping("/{raceId}/dogs")
    public ResponseEntity<List<DogDto>> getDogsInRace(@PathVariable Long raceId) {
        List<Dog> dogs = raceService.getDogsInRace(raceId);
        List<DogDto> dogDtos = DogMapper.getDogDtos(dogs);
        return ResponseEntity.ok(dogDtos);
    }

    @PostMapping
    public ResponseEntity<RaceDto> addRace(@RequestBody RaceRequestDto raceDto) {
        Race savedRace = raceService.addRace(raceDto);
        RaceDto savedRaceDto = RaceMapper.remapRace(savedRace);
        return ResponseEntity.ok(savedRaceDto);
    }

    @PutMapping("/{raceId}")
    public ResponseEntity<RaceDto> updateRace(@PathVariable Long raceId, @RequestBody RaceRequestDto raceDto) {
        Race updatedRace = raceService.updateRace(raceId, raceDto);
        RaceDto updatedRaceDto = RaceMapper.remapRace(updatedRace);
        return ResponseEntity.ok(updatedRaceDto);
    }

    @DeleteMapping("/{raceId}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long raceId) {
        raceService.deleteRace(raceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/participations")
    public ResponseEntity<RaceParticipationDto> addRaceParticipation(@RequestBody RaceParticipationRequestDto participationDto) {
        RaceParticipation participation = raceService.addRaceParticipation(participationDto);
        return ResponseEntity.ok(RaceParticipationMapper.remapRaceParticipation(participation));
    }

    @PutMapping("/participations/{id}")
    public ResponseEntity<RaceParticipationDto> updateRaceParticipation(@PathVariable Long id, @RequestBody RaceParticipationRequestDto participationDto) {
        RaceParticipation updatedParticipation = raceService.updateRaceParticipation(id, participationDto);
        return ResponseEntity.ok(RaceParticipationMapper.remapRaceParticipation(updatedParticipation));
    }

    @DeleteMapping("/participations/{id}")
    public ResponseEntity<Void> deleteRaceParticipation(@PathVariable Long id) {
        raceService.deleteRaceParticipation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/participations/{id}")
    public ResponseEntity<RaceParticipationDto> getRaceParticipation(@PathVariable Long id) {
        RaceParticipation participation = raceService.getRaceParticipation(id);
        return ResponseEntity.ok(RaceParticipationMapper.remapRaceParticipation(participation));
    }



}

