package com.casumo.races.mapper;

import com.casumo.races.dto.RaceDto;
import com.casumo.races.db.Race;

import java.util.List;
import java.util.stream.Collectors;

public class RaceMapper {

    public static RaceDto remapRace(Race race) {
        if (race == null) return null;

        return new RaceDto(race.getId(), race.getName(), race.getRaceTime());
    }

    public static List<RaceDto> getRaceDtos(List<Race> races) {

        if (races.isEmpty()) return null;

        return races.stream()
                .map(RaceMapper::remapRace)
                .collect(Collectors.toList());
    }
}