package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.dto.PersonDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String type;

    private int typeNumber;

    private String status;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Person> persons = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "movie_country",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private Set<Country> countries = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public MovieDTO toDTO() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(this.id);
        movieDTO.setName(this.name);
        movieDTO.setDescription(this.description);
        movieDTO.setType(this.type);
        movieDTO.setTypeNumber(this.typeNumber);
        movieDTO.setStatus(this.status);

        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : this.persons) {
            personDTOList.add(person.toDTO());
        }
        movieDTO.setPersons(personDTOList);

        Set<Long> genreIds = new HashSet<>();
        for (Genre genre : this.genres) {
            genreIds.add(genre.getId());
        }
        movieDTO.setGenreIds(genreIds);

        Set<Long> countryIds = new HashSet<>();
        for (Country country : this.countries) {
            countryIds.add(country.getId());
        }
        movieDTO.setCountryIds(countryIds);

        return movieDTO;
    }

    public static Movie fromDTO(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setType(movieDTO.getType());
        movie.setTypeNumber(movieDTO.getTypeNumber());
        movie.setStatus(movieDTO.getStatus());

        // Устанавливаем персоны
        List<Person> persons = new ArrayList<>();
        for (PersonDTO personDTO : movieDTO.getPersons()) {
            Person person = new Person();
            person.setName(personDTO.getName());
            person.setDescription(personDTO.getDescription());
            // Устанавливаем связь с фильмом
            person.setMovie(movie);
            persons.add(person);
        }
        movie.setPersons(persons);

        // Устанавливаем жанры
        Set<Genre> genres = new HashSet<>();
        for (Long genreId : movieDTO.getGenreIds()) {
            Genre genre = new Genre();
            genre.setId(genreId); // Устанавливаем только идентификатор
            genres.add(genre);
        }
        movie.setGenres(genres);

        // Устанавливаем страны
        Set<Country> countries = new HashSet<>();
        for (Long countryId : movieDTO.getCountryIds()) {
            Country country = new Country();
            country.setId(countryId); // Устанавливаем только идентификатор
            countries.add(country);
        }
        movie.setCountries(countries);

        return movie;
    }
}
