package com.casumo.races.db;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "race_participation")
public class RaceParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "race_participation_seq_generator")
    @SequenceGenerator(name = "race_participation_seq_generator", sequenceName = "race_participation_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @ManyToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    private double odds;

    @OneToMany(mappedBy = "raceParticipation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Bet> bets = new HashSet<>();

}

