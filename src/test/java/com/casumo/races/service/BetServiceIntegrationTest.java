package com.casumo.races.service;

import com.casumo.races.db.*;
import com.casumo.races.dto.BetRequestDto;
import com.casumo.races.exception.RacesException;
import com.casumo.races.repository.BetRepository;
import com.casumo.races.repository.DogRepository;
import com.casumo.races.repository.RaceParticipationRepository;
import com.casumo.races.repository.RaceRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.casumo.races.repository.UserRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class BetServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:11.1");

    @BeforeAll
    static void beforeAll() {
        postgresContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgresContainer.stop();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
    }

    @Autowired
    private BetService betService;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private RaceParticipationRepository raceParticipationRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private UserRepository userRepository;

    private RacesUser validUser;
    private Race race;
    private Dog dog;
    private RaceParticipation raceParticipation;

    @BeforeEach
    void setUp() {
        validUser = userRepository.save(new RacesUser(null, UserType.CUSTOMER, "test", "test","$2a$12$V9371id/4/69Blhu0HUV2On3Lc9VNfI421boAmKMy4MRunTBQ4SOO","testuser@example.com", new HashSet<>()));
        race = raceRepository.save(new Race(null, "Derby", null, null));
        dog = dogRepository.save(new Dog(null, "Bolt", 3, "Greyhound", null));
        raceParticipation = raceParticipationRepository.save(new RaceParticipation(null, race, dog, 2.5, null));
    }

    @Test
    public void testPlaceBet_UserNotFound() {
        BetRequestDto betRequest = new BetRequestDto(Long.MAX_VALUE, race.getId(), dog.getId(), new BigDecimal("100"));
        assertThrows(RacesException.class, () -> betService.placeBet(betRequest), "User not found!");
    }

    @Test
    public void testPlaceBet_RaceParticipationNotFound() {
        Long nonExistentRaceId = Long.MAX_VALUE;
        Long nonExistentDogId = Long.MAX_VALUE;
        BetRequestDto betRequest = new BetRequestDto(validUser.getId(), nonExistentRaceId, nonExistentDogId, new BigDecimal("100"));
        assertThrows(RacesException.class, () -> betService.placeBet(betRequest), "Race Participation not found!");
    }

    @Test
    @Transactional
    public void testPlaceBet() {
        Race race = raceRepository.save(new Race(null, "Test Race", new Timestamp(System.currentTimeMillis()), null));
        Dog dog = dogRepository.save(new Dog(null, "Test Dog", 3, "Breed", null));
        raceParticipationRepository.save(new RaceParticipation(null, race, dog, 2.5, null));

        BetRequestDto betRequest = new BetRequestDto(validUser.getId(), race.getId(), dog.getId(), new BigDecimal("100"));
        Bet placedBet = betService.placeBet(betRequest);

        assertThat(placedBet).isNotNull();
        assertThat(placedBet.getAmount()).isEqualByComparingTo("100");
        assertThat(placedBet.getRaceParticipation().getRace()).isEqualTo(race);
        assertThat(placedBet.getRaceParticipation().getDog()).isEqualTo(dog);
    }


}
