package com.casumo.races.controller;

import com.casumo.races.config.TestSecurityConfig;
import com.casumo.races.dto.DogRequestDto;
import com.casumo.races.db.Dog;
import com.casumo.races.repository.DogRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class DogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DogRepository dogRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    public void clearDatabase() {
        dogRepository.deleteAll();
    }


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    public void testGetAllDogs() throws Exception {
        Dog dog = new Dog(null, "Buddy", 3, "Golden Retriever", null);
        dogRepository.save(dog);

        mockMvc.perform(get("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name", is("Buddy")))
                .andExpect(jsonPath("$[0].age", is(3)))
                .andExpect(jsonPath("$[0].breed", is("Golden Retriever")));
    }

    @Test
    public void testAddDog() throws Exception {
        DogRequestDto newDogDto = new DogRequestDto("Maxx2", 12, "Labradore");

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Maxx2\",\"age\":12,\"breed\":\"Labradore\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Maxx2")))
                .andExpect(jsonPath("$.age", is(12)))
                .andExpect(jsonPath("$.breed", is("Labradore")));

        List<Dog> dogs = dogRepository.findAll();
        assert dogs.size() == 1;
        Dog savedDog = dogs.get(0);
        assert savedDog.getName().equals(newDogDto.name());
        assert savedDog.getAge() == newDogDto.age();
        assert savedDog.getBreed().equals(newDogDto.breed());
    }
}
