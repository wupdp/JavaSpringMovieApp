package by.bondarev.dbms;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
public class MovieEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Container
    private static final DockerComposeContainer<?> dockerComposeContainer =
            new DockerComposeContainer<>(new File("../test-docker-compose.yml"))
                    .withExposedService("db-container", 5432, Wait.forListeningPort())
                    .withExposedService("dbms-service", 8080, Wait.forListeningPort());

    @Test
    public void testSaveAndRetrieveMovie() {
        Type type = new Type();
        type.setName("TEST");
        entityManager.persistAndFlush(type);

        Status status = new Status();
        status.setName("TEST");
        entityManager.persistAndFlush(status);

        Set<Person> persons = new HashSet<>();
        Person person1 = new Person();
        person1.setName("TEST1");
        Person person2 = new Person();
        person2.setName("TEST2");
        entityManager.persistAndFlush(person1);
        entityManager.persistAndFlush(person2);
        persons.add(person1);
        persons.add(person2);

        Set<Genre> genres = new HashSet<>();
        Genre genre1 = new Genre();
        genre1.setName("TEST1");
        Genre genre2 = new Genre();
        genre2.setName("TEST2");
        entityManager.persistAndFlush(genre1);
        entityManager.persistAndFlush(genre2);
        genres.add(genre1);
        genres.add(genre2);

        Set<Country> countries = new HashSet<>();
        Country country = new Country();
        country.setName("TEST");
        entityManager.persistAndFlush(country);
        countries.add(country);

        Movie movie = new Movie();
        movie.setName("Test Movie");
        movie.setDescription("Test Description");
        movie.setType(type);
        movie.setStatus(status);
        movie.setPersons(persons);
        movie.setGenres(genres);
        movie.setCountries(countries);

        entityManager.persistAndFlush(movie);

        Long movieId = movie.getId();
        assertNotNull(movieId);

        Movie retrievedMovie = entityManager.find(Movie.class, movieId);
        assertNotNull(retrievedMovie);
        assertEquals("Test Movie", retrievedMovie.getName());
        assertEquals("Test Description", retrievedMovie.getDescription());
        assertEquals(type, retrievedMovie.getType());
        assertEquals(status, retrievedMovie.getStatus());
        assertEquals(persons, retrievedMovie.getPersons());
        assertEquals(genres, retrievedMovie.getGenres());
        assertEquals(countries, retrievedMovie.getCountries());

        entityManager.clear();
    }

    @Test
    public void testMovieDTOConversion() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setName("Test Movie");
        movie.setDescription("Test Description");

        Type type = new Type();
        type.setName("TEST_Converted");
        movie.setType(type);

        Status status = new Status();
        status.setName("TEST_Converted");
        movie.setStatus(status);

        Set<Person> persons = new HashSet<>();
        Person person1 = new Person();
        person1.setName("TEST_Converted1");
        Person person2 = new Person();
        person2.setName("TEST_Converted2");
        persons.add(person1);
        persons.add(person2);
        movie.setPersons(persons);

        Set<Genre> genres = new HashSet<>();
        Genre genre1 = new Genre();
        genre1.setName("TEST_Converted1");
        Genre genre2 = new Genre();
        genre2.setName("TEST_Converted2");
        genres.add(genre1);
        genres.add(genre2);
        movie.setGenres(genres);

        Set<Country> countries = new HashSet<>();
        Country country = new Country();
        country.setName("TEST_Converted");
        countries.add(country);
        movie.setCountries(countries);

        MovieDTO movieDTO = movie.toDTO();

        assertEquals(movie.getId(), movieDTO.getId());
        assertEquals(movie.getName(), movieDTO.getName());
        assertEquals(movie.getDescription(), movieDTO.getDescription());
        assertEquals(type.getName(), movieDTO.getType());
        assertEquals(type.getId(), movieDTO.getTypeNumber());
        assertEquals(status.getName(), movieDTO.getStatus());
        assertEquals(status.getId(), movieDTO.getStatusId());
        assertEquals(persons.size(), movieDTO.getPersons().size());
        assertEquals(genres.size(), movieDTO.getGenres().size());
        assertEquals(countries.size(), movieDTO.getCountries().size());

        entityManager.clear();
    }
}
