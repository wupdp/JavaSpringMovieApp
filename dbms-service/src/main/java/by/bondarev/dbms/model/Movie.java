package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.MovieDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "type_id")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "movie_person",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> persons = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,  CascadeType.PERSIST })
    @JoinTable(name = "movie_country",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private Set<Country> countries = new HashSet<>();


    public MovieDTO toDTO() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(this.id);
        movieDTO.setName(this.name);
        movieDTO.setDescription(this.description);
        movieDTO.setType(this.type.getName());
        movieDTO.setTypeNumber(this.type.getId());
        movieDTO.setStatus(this.status.getName());
        movieDTO.setStatusId(this.status.getId());
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

        Type type = new Type();
        type.setId(movieDTO.getTypeNumber());
        type.setName(movieDTO.getType());

        movie.setType(type);

        Status status = new Status();
        status.setId(movieDTO.getStatusId());
        status.setName(movieDTO.getStatus());

        movie.setStatus(status);
        // Преобразование наборов DTO в наборы entities
        movie.setPersons(movieDTO.getPersons().stream().map(Person::fromDTO).collect(Collectors.toSet()));
        movie.setGenres(movieDTO.getGenres().stream().map(Genre::fromDTO).collect(Collectors.toSet()));
        movie.setCountries(movieDTO.getCountries().stream().map(Country::fromDTO).collect(Collectors.toSet()));
        return movie;
    }
}
