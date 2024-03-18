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
@Table(name = "dog")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dog_seq_generator")
    @SequenceGenerator(name = "dog_seq_generator", sequenceName = "dog_seq", allocationSize = 1)
    private Long id;

    private String name;

    private int age;

    private String breed;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RaceParticipation> raceParticipations = new HashSet<>();

}

