package com.casumo.races.controller;

import com.casumo.races.dto.BetDto;
import com.casumo.races.dto.BetRequestDto;
import com.casumo.races.db.Bet;
import com.casumo.races.mapper.BetMapper;
import com.casumo.races.service.BetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    @PostMapping
    public ResponseEntity<BetDto> placeBet(@RequestBody BetRequestDto betRequest) {
        Bet bet = betService.placeBet(betRequest);
        BetDto betDto = BetMapper.remapBet(bet);
        return ResponseEntity.ok(betDto);
    }

    @PutMapping("/{betId}")
    public ResponseEntity<BetDto> updateBet(@PathVariable Long betId, @RequestParam BigDecimal newAmount) {
        Bet updatedBet = betService.updateBet(betId, newAmount);
        BetDto updatedBetDto = BetMapper.remapBet(updatedBet);
        return ResponseEntity.ok(updatedBetDto);
    }

    @DeleteMapping("/{betId}")
    public ResponseEntity<Void> deleteBet(@PathVariable Long betId) {
        betService.deleteBet(betId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{betId}")
    public ResponseEntity<BetDto> getBet(@PathVariable Long betId) {
        Bet bet = betService.getBet(betId);
        BetDto betDto = BetMapper.remapBet(bet);
        return ResponseEntity.ok(betDto);
    }
}

