package com.casumo.races.service;

import com.casumo.races.dto.BetRequestDto;
import com.casumo.races.db.Bet;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.BetRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import com.casumo.races.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class BetService {

    private final BetRepository betRepository;
    private final RaceParticipationRepository raceParticipationRepository;
    private final UserRepository userRepository;

    public Bet placeBet(BetRequestDto betRequest) {
        var user = userRepository.getRacesUserById(betRequest.userId());
        if (user == null) {
            throw new RacesException("User not found !", "User not found!", HttpStatus.NOT_FOUND);
        }

        var raceParticipation = raceParticipationRepository.findByRaceIdAndDogId(betRequest.raceId(), betRequest.dogId());
        if (raceParticipation == null) {
            throw new RacesException("Race Participation not found !", "Race Participation not found !", HttpStatus.NOT_FOUND);
        }

        Bet bet = new Bet();
        bet.setUser(user);
        bet.setRaceParticipation(raceParticipation);
        bet.setAmount(betRequest.amount());
        user.getBets().add(bet);
        return betRepository.save(bet);
    }

    public void deleteBet(Long betId) {
        if (!betRepository.existsById(betId)) {
            throw new RacesException("Bet not found !", "Bet not found !", HttpStatus.NOT_FOUND);
        }
        betRepository.deleteById(betId);
    }

    public Bet updateBet(Long betId, BigDecimal newAmount) {
        Optional<Bet> existingBet = betRepository.findById(betId);
        if (existingBet.isEmpty()) {
            throw new RacesException("Bet not found !", "Bet not found !", HttpStatus.NOT_FOUND);
        }
        Bet bet = existingBet.get();
        bet.setAmount(newAmount);
        return betRepository.save(bet);
    }

    public Bet getBet(Long betId) {
        return betRepository.findById(betId).orElseThrow(() ->
                new RacesException("Bet not found !", "Bet not found !", HttpStatus.NOT_FOUND));
    }


}
