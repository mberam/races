package com.casumo.races.dto;


import java.sql.Timestamp;

public record RaceRequestDto(String name, Timestamp raceTime) {
}
