package com.casumo.races.repository;

import com.casumo.races.db.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    void deleteByRaceParticipationId(Long id);
}
