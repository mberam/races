package com.casumo.races.mapper;

import com.casumo.races.dto.RaceParticipationDto;
import com.casumo.races.db.RaceParticipation;

public class RaceParticipationMapper {

    public static RaceParticipationDto remapRaceParticipation(RaceParticipation participation) {
        if (participation == null) {
            return null;
        }
        return new RaceParticipationDto(
                participation.getId(),
                participation.getRace().getId(),
                participation.getDog().getId(),
                participation.getOdds()
        );
    }
}
