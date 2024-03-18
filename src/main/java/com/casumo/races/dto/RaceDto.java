package com.casumo.races.dto;

import java.sql.Timestamp;

public record RaceDto(Long raceId, String name, Timestamp raceTime) {
}
