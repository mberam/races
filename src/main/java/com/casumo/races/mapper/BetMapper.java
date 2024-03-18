package com.casumo.races.mapper;

import com.casumo.races.dto.BetDto;
import com.casumo.races.db.Bet;


public class BetMapper {
    public static BetDto remapBet(Bet bet) {
        if (bet == null || bet.getRaceParticipation() == null ||
                bet.getRaceParticipation().getRace() == null ||
                bet.getRaceParticipation().getDog() == null) {
            return null;
        }

        return new BetDto(bet.getId(),bet.getRaceParticipation().getRace().getId(), bet.getRaceParticipation().getDog().getId(), bet.getAmount());
    }
}

