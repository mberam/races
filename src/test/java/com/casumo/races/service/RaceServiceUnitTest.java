package com.casumo.races.service;

import com.casumo.races.db.Dog;
import com.casumo.races.db.Race;
import com.casumo.races.db.RaceParticipation;
import com.casumo.races.dto.RaceParticipationRequestDto;
import com.casumo.races.dto.RaceRequestDto;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.DogRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import com.casumo.races.repository.RaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RaceServiceUnitTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private RaceParticipationRepository raceParticipationRepository;

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private RaceService raceService;

    private Race validRace;
    private Dog validDog;
    private RaceRequestDto validRaceRequestDto;

    @BeforeEach
    void setUp() {
        validRace = new Race(1L, "Derby", new Timestamp(System.currentTimeMillis()), null);
        validRaceRequestDto = new RaceRequestDto("Derby", new Timestamp(System.currentTimeMillis()));
        validDog = new Dog(1L, "Buddy", 5, "Labrador",null);
    }

    @AfterEach
    void tearDown() {
        clearInvocations(raceRepository, raceParticipationRepository);
    }

    @Test
    void whenFindAllRacesCalled_thenReturnRaceList() {
        when(raceRepository.findAll()).thenReturn(List.of(validRace));
        List<Race> raceList = raceService.findAllRaces();

        assertNotNull(raceList);
        assertFalse(raceList.isEmpty());
        assertEquals(validRace, raceList.get(0));
    }

    @Test
    void whenUpdateNonExistentRace_thenThrowException() {
        when(raceRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RacesException.class, () -> raceService.updateRace(1L, validRaceRequestDto));
    }


    @Test
    void whenAddRaceParticipationCalled_thenSaveParticipation() {

        RaceParticipationRequestDto participationRequestDto = new RaceParticipationRequestDto(1L, 1L, 5.0);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(validRace));
        when(dogRepository.findById(1L)).thenReturn(Optional.of(validDog));

        RaceParticipation expectedParticipation = new RaceParticipation(1L, validRace, validDog, 5.0, null);
        when(raceParticipationRepository.save(any(RaceParticipation.class))).thenReturn(expectedParticipation);

        RaceParticipation result = raceService.addRaceParticipation(participationRequestDto);

        assertNotNull(result);
        assertEquals(validRace, result.getRace());
        assertEquals(validDog, result.getDog());
        assertEquals(5.0, result.getOdds());
    }

    @Test
    void whenGetDogsInRaceCalled_thenReturnListOfDogs() {
        RaceParticipation participation = new RaceParticipation(1L, validRace, validDog, 5.0, null);
        when(raceParticipationRepository.findByRaceId(1L)).thenReturn(List.of(participation));

        List<Dog> dogs = raceService.getDogsInRace(1L);

        assertNotNull(dogs);
        assertFalse(dogs.isEmpty());
        assertTrue(dogs.contains(validDog));
    }

    @Test
    void whenUpdateRaceParticipationCalled_thenSuccessfullyUpdate() {
        RaceParticipationRequestDto requestDto = new RaceParticipationRequestDto(1L, 1L, 2.0);
        RaceParticipation existingParticipation = new RaceParticipation(1L, validRace, validDog, 5.0, null);

        when(raceParticipationRepository.findById(1L)).thenReturn(Optional.of(existingParticipation));
        when(raceRepository.findById(1L)).thenReturn(Optional.of(validRace));
        when(dogRepository.findById(1L)).thenReturn(Optional.of(validDog));
        when(raceParticipationRepository.save(any(RaceParticipation.class))).thenReturn(existingParticipation);

        RaceParticipation updatedParticipation = raceService.updateRaceParticipation(1L, requestDto);

        assertNotNull(updatedParticipation);
        assertEquals(2.0, updatedParticipation.getOdds());
    }

    @Test
    void whenUpdateRaceParticipationForNonExistent_thenThrowException() {
        RaceParticipationRequestDto requestDto = new RaceParticipationRequestDto(1L, 1L, 3.0);

        when(raceParticipationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RacesException.class, () -> raceService.updateRaceParticipation(1L, requestDto));
    }

    @Test
    void whenDeleteRaceParticipationCalled_thenSuccessfullyDelete() {
        when(raceParticipationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(raceParticipationRepository).deleteById(1L);

        assertDoesNotThrow(() -> raceService.deleteRaceParticipation(1L));
    }

    @Test
    void whenDeleteRaceParticipationForNonExistent_thenThrowException() {
        when(raceParticipationRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(RacesException.class, () -> raceService.deleteRaceParticipation(1L));
    }

}
