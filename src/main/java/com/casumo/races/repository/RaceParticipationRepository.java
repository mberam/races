package com.casumo.races.repository;

import com.casumo.races.db.RaceParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceParticipationRepository extends JpaRepository<RaceParticipation, Long> {
    List<RaceParticipation> findByRaceId(Long raceId);
    List<RaceParticipation> findByDogId(Long dogId);

    RaceParticipation findByRaceIdAndDogId(Long raceId, Long dogId);
}

