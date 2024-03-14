package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.MovieDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String type;

    @Column(name = "type_number")
    private int typeNumber;

    private String status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "movie_person",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> persons = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "movie_country",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private Set<Country> countries = new HashSet<>();


    public MovieDTO toDTO() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(this.id);
        movieDTO.setName(this.name);
        movieDTO.setDescription(this.description);
        movieDTO.setType(this.type);
        movieDTO.setTypeNumber(this.typeNumber);
        movieDTO.setStatus(this.status);
        // Преобразование наборов entities в наборы DTO
        movieDTO.setPersons(this.persons.stream().map(Person::toDTO).collect(Collectors.toSet()));
        movieDTO.setGenres(this.genres.stream().map(Genre::toDTO).collect(Collectors.toSet()));
        movieDTO.setCountries(this.countries.stream().map(Country::toDTO).collect(Collectors.toSet()));
        return movieDTO;
    }

    public static Movie fromDTO(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setType(movieDTO.getType());
        movie.setTypeNumber(movieDTO.getTypeNumber());
        movie.setStatus(movieDTO.getStatus());
        // Преобразование наборов DTO в наборы entities
        movie.setPersons(movieDTO.getPersons().stream().map(Person::fromDTO).collect(Collectors.toSet()));
        movie.setGenres(movieDTO.getGenres().stream().map(Genre::fromDTO).collect(Collectors.toSet()));
        movie.setCountries(movieDTO.getCountries().stream().map(Country::fromDTO).collect(Collectors.toSet()));
        return movie;
    }
}
