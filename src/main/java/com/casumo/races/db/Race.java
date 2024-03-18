package com.casumo.races.db;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "race")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "race_seq_generator")
    @SequenceGenerator(name = "race_seq_generator", sequenceName = "race_seq", allocationSize = 1)
    private Long id;

    private String name;

    @Column(name = "race_time")
    private Timestamp raceTime;

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RaceParticipation> raceParticipations = new HashSet<>();

}
