
package com.casumo.races.service;

import com.casumo.races.db.*;
import com.casumo.races.dto.BetRequestDto;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.BetRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import com.casumo.races.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BetServiceUnitTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private RaceParticipationRepository raceParticipationRepository;

    @InjectMocks
    private BetService betService;

    @Mock
    private UserRepository userRepository;

    private Race race;
    private Dog dog;
    private RaceParticipation raceParticipation;

    @BeforeEach
    void setUp() {
        race = new Race(1L, "Test Race", null, null);
        dog = new Dog(1L, "Test Dog", 3, "Breed", null);
        raceParticipation = new RaceParticipation(1L, race, dog, 2.5, null);
    }

    @Test
    void placeBet_Success() {
        BetRequestDto betRequest = new BetRequestDto(1L, race.getId(), dog.getId(), new BigDecimal("100"));
        when(raceParticipationRepository.findByRaceIdAndDogId(race.getId(), dog.getId()))
                .thenReturn(raceParticipation);
        when(betRepository.save(any(Bet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.getRacesUserById(anyLong())).thenReturn(new RacesUser());

        Bet result = betService.placeBet(betRequest);

        assertNotNull(result);
        assertEquals(raceParticipation, result.getRaceParticipation());
        assertEquals(new BigDecimal("100"), result.getAmount());
        assertNotNull(result.getUser());
    }

    @Test
    void deleteBet_Success() {
        Long betId = 1L;
        when(betRepository.existsById(betId)).thenReturn(true);
        assertDoesNotThrow(() -> betService.deleteBet(betId));
        verify(betRepository).deleteById(betId);
    }

    @Test
    void deleteBet_Failure_BetNotFound() {
        Long betId = 1L;
        when(betRepository.existsById(betId)).thenReturn(false);
        assertThrows(RacesException.class, () -> betService.deleteBet(betId), "Bet not found !");
    }

    @Test
    void updateBet_Success() {
        Long betId = 1L;
        BigDecimal newAmount = new BigDecimal("200");
        when(betRepository.findById(betId)).thenReturn(Optional.of(new Bet(betId, raceParticipation, new BigDecimal("100"), null)));
        when(betRepository.save(any(Bet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bet updatedBet = betService.updateBet(betId, newAmount);

        assertNotNull(updatedBet);
        assertEquals(newAmount, updatedBet.getAmount());
    }

    @Test
    void updateBet_Failure_BetNotFound() {
        Long betId = 1L;
        BigDecimal newAmount = new BigDecimal("200");
        when(betRepository.findById(betId)).thenReturn(Optional.empty());
        assertThrows(RacesException.class, () -> betService.updateBet(betId, newAmount), "Bet not found !");
    }

    @Test
    void getBet_Success() {
        Long betId = 1L;
        when(betRepository.findById(betId)).thenReturn(Optional.of(new Bet(betId, raceParticipation, new BigDecimal("100"), null)));

        Bet foundBet = betService.getBet(betId);

        assertNotNull(foundBet);
        assertEquals(betId, foundBet.getId());
        assertEquals(new BigDecimal("100"), foundBet.getAmount());
    }

    @Test
    void getBet_Failure_BetNotFound() {
        Long betId = 1L;
        when(betRepository.findById(betId)).thenReturn(Optional.empty());
        assertThrows(RacesException.class, () -> betService.getBet(betId), "Bet not found !");
    }





    @Test
    void placeBet_Failure_NoSuchDogInRace() {
        BetRequestDto betRequest = new BetRequestDto(1L, race.getId(), dog.getId(), new BigDecimal("100"));

        when(raceParticipationRepository.findByRaceIdAndDogId(race.getId(), dog.getId())).thenReturn(null);
        when(userRepository.getRacesUserById(anyLong())).thenReturn(new RacesUser());

        assertThrows(RacesException.class, () -> betService.placeBet(betRequest),
                "Race Participation not found !");

        verify(raceParticipationRepository).findByRaceIdAndDogId(race.getId(), dog.getId());
        verify(betRepository, never()).save(any(Bet.class));
    }

}


