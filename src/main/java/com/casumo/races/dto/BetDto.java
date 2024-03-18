package com.casumo.races.dto;


import java.math.BigDecimal;

public record BetDto(Long id, Long raceId, Long dogId, BigDecimal amount) {
}
