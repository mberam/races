package com.casumo.races.db;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bet")
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bet_seq_generator")
    @SequenceGenerator(name = "bet_seq_generator", sequenceName = "bet_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "race_participation_id")
    private RaceParticipation raceParticipation;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private RacesUser user;



}

