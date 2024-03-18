package com.casumo.races.dto;

import java.math.BigDecimal;

public record BetRequestDto(Long userId, Long raceId, Long dogId, BigDecimal amount) {
}


