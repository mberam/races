package com.casumo.races.service;

import com.casumo.races.db.Dog;
import com.casumo.races.db.Race;
import com.casumo.races.db.RaceParticipation;
import com.casumo.races.dto.RaceParticipationRequestDto;
import com.casumo.races.dto.RaceRequestDto;
import com.casumo.races.repository.DogRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import com.casumo.races.repository.RaceRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.util.List;

import static com.casumo.races.service.DogServiceIntegrationTest.postgreSQLContainer;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RaceService.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RaceServiceIntegrationTest {

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:11.1");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
    }

    @Autowired
    private RaceService raceService;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private RaceParticipationRepository raceParticipationRepository;

    @Test
    public void testGetDogsInRace() {
        Race race = raceRepository.save(new Race(null, "Spring Derby", new Timestamp(System.currentTimeMillis()), null));
        Dog dog = dogRepository.save(new Dog(null, "Bolt", 4, "Greyhound", null));
        raceParticipationRepository.save(new RaceParticipation(null, race, dog, 2.5, null));

        List<Dog> dogsInRace = raceService.getDogsInRace(race.getId());

        assertThat(dogsInRace).hasSize(1);
        assertThat(dogsInRace.get(0).getName()).isEqualTo("Bolt");
    }

    @Test
    public void testAddRace() {
        RaceRequestDto raceRequestDto = new RaceRequestDto("Summer Sprint", new Timestamp(System.currentTimeMillis()));
        Race savedRace = raceService.addRace(raceRequestDto);

        assertThat(savedRace).isNotNull();
        assertThat(savedRace.getName()).isEqualTo(raceRequestDto.name());
    }

    @Test
    public void testUpdateRace() {
        Race race = raceRepository.save(new Race(null, "Autumn Run", new Timestamp(System.currentTimeMillis()), null));
        RaceRequestDto raceRequestDto = new RaceRequestDto("Autumn Run Updated", new Timestamp(System.currentTimeMillis()));

        Race updatedRace = raceService.updateRace(race.getId(), raceRequestDto);

        assertThat(updatedRace.getName()).isEqualTo("Autumn Run Updated");
    }

    @Test
    public void testDeleteRace() {
        Race race = raceRepository.save(new Race(null, "Winter Challenge", new Timestamp(System.currentTimeMillis()), null));
        assertThat(raceRepository.existsById(race.getId())).isTrue();

        raceService.deleteRace(race.getId());

        assertThat(raceRepository.existsById(race.getId())).isFalse();
    }

    @Test
    public void testAddRaceParticipation() {
        Race race = raceRepository.save(new Race(null, "Spring Derby", new Timestamp(System.currentTimeMillis()), null));
        Dog dog = dogRepository.save(new Dog(null, "Flash", 3, "Whippet", null));
        RaceParticipationRequestDto participationRequestDto = new RaceParticipationRequestDto(race.getId(), dog.getId(), 3.0);

        RaceParticipation participation = raceService.addRaceParticipation(participationRequestDto);

        assertThat(participation).isNotNull();
        assertThat(participation.getRace().getId()).isEqualTo(race.getId());
        assertThat(participation.getDog().getId()).isEqualTo(dog.getId());
        assertThat(participation.getOdds()).isEqualTo(3.0);
    }

    @Test
    public void testUpdateRaceParticipation() {
        Race race = raceRepository.save(new Race(null, "Summer Sprint", new Timestamp(System.currentTimeMillis()), null));
        Dog dog = dogRepository.save(new Dog(null, "Lightning", 2, "Saluki", null));
        RaceParticipation participation = raceParticipationRepository.save(new RaceParticipation(null, race, dog, 4.0, null));
        RaceParticipationRequestDto updatedParticipationDto = new RaceParticipationRequestDto(race.getId(), dog.getId(), 5.0);

        RaceParticipation updatedParticipation = raceService.updateRaceParticipation(participation.getId(), updatedParticipationDto);

        assertThat(updatedParticipation.getOdds()).isEqualTo(5.0);
    }

    @Test
    public void testDeleteRaceParticipation() {
        Race race = raceRepository.save(new Race(null, "Fall Classic", new Timestamp(System.currentTimeMillis()), null));
        Dog dog = dogRepository.save(new Dog(null, "Storm", 5, "Doberman", null));
        RaceParticipation participation = raceParticipationRepository.save(new RaceParticipation(null, race, dog, 6.0, null));

        assertThat(raceParticipationRepository.existsById(participation.getId())).isTrue();

        raceService.deleteRaceParticipation(participation.getId());

        assertThat(raceParticipationRepository.existsById(participation.getId())).isFalse();
    }

}

