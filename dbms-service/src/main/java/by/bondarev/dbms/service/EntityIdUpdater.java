package by.bondarev.dbms.service;

import by.bondarev.dbms.dto.CountryDTO;
import by.bondarev.dbms.dto.GenreDTO;
import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.dto.PersonDTO;
import by.bondarev.dbms.repository.CountryRepository;
import by.bondarev.dbms.repository.GenreRepository;
import by.bondarev.dbms.repository.MovieRepository;
import by.bondarev.dbms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EntityIdUpdater {

    private final PersonRepository personRepository;

    private final GenreRepository genreRepository;

    private final CountryRepository countryRepository;

    private final MovieRepository movieRepository;

    @Autowired
    public EntityIdUpdater(PersonRepository personRepository, GenreRepository genreRepository, CountryRepository countryRepository, MovieRepository movieRepository) {
        this.personRepository = personRepository;
        this.genreRepository = genreRepository;
        this.countryRepository = countryRepository;
        this.movieRepository = movieRepository;
    }

    public Set<PersonDTO> updatePersonsIds(Set<PersonDTO> personDTOSet) {
        if (personDTOSet == null) return Collections.emptySet();

        Set<PersonDTO> updatedPersonDTOSet = new HashSet<>();
        for (PersonDTO personDTO : personDTOSet) {
            Optional<Long> existingPersonIdOptional = personRepository.getIdByName(personDTO.getName());
            existingPersonIdOptional.ifPresent(personDTO::setId);
            updatedPersonDTOSet.add(personDTO);
        }
        return updatedPersonDTOSet;
    }

    public Set<GenreDTO> updateGenresIds(Set<GenreDTO> genreDTOSet) {
        if (genreDTOSet == null) return Collections.emptySet();

        Set<GenreDTO> updatedGenreDTOSet = new HashSet<>();
        for (GenreDTO genreDTO : genreDTOSet) {
            Optional<Long> existingGenreIdOptional = genreRepository.getIdByName(genreDTO.getName());
            existingGenreIdOptional.ifPresent(genreDTO::setId);
            updatedGenreDTOSet.add(genreDTO);
        }
        return updatedGenreDTOSet;
    }

    public Set<CountryDTO> updateCountriesIds(Set<CountryDTO> countryDTOSet) {
        if (countryDTOSet == null) return Collections.emptySet();

        Set<CountryDTO> updatedCountryDTOSet = new HashSet<>();
        for (CountryDTO countryDTO : countryDTOSet) {
            Optional<Long> existingCountryIdOptional = countryRepository.getIdByName(countryDTO.getName());
            existingCountryIdOptional.ifPresent(countryDTO::setId);
            updatedCountryDTOSet.add(countryDTO);
        }
        return updatedCountryDTOSet;
    }

    public Set<MovieDTO> updateMoviesIds(Set<MovieDTO> movieDTOSet) {
        if (movieDTOSet == null) return Collections.emptySet();

        Set<MovieDTO> updatedMovieDTOSet = new HashSet<>();
        for (MovieDTO movieDTO : movieDTOSet) {
            Optional<Long> existingMovieIdOptional = movieRepository.getIdByName(movieDTO.getName());
            existingMovieIdOptional.ifPresent(movieDTO::setId);
            updatedMovieDTOSet.add(movieDTO);
        }
        return updatedMovieDTOSet;
    }


}
