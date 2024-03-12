package by.bondarev.dbms.service;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.dto.PersonDTO;
import by.bondarev.dbms.model.Country;
import by.bondarev.dbms.model.Genre;
import by.bondarev.dbms.model.Movie;
import by.bondarev.dbms.model.Person;
import by.bondarev.dbms.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final CountryService countryService;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreService genreService, CountryService countryService) {
        this.movieRepository = movieRepository;
        this.genreService = genreService;
        this.countryService = countryService;
    }



    public List<Movie> findMoviesByGenre(String genreName) {
        return movieRepository.findByGenresName(genreName);
    }

    public List<Movie> findMoviesByCountry(String countryName) {
        return movieRepository.findByCountriesName(countryName);
    }

    public List<Movie> findMoviesByPerson(String personName) {
        return movieRepository.findByPersonName(personName);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie(MovieDTO movieDTO) {
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
            Genre genre = genreService.getGenreById(genreId).orElseThrow(() -> new IllegalArgumentException("Genre not found with id: " + genreId));
            genres.add(genre);
        }
        movie.setGenres(genres);

        // Устанавливаем страны
        Set<Country> countries = new HashSet<>();
        for (Long countryId : movieDTO.getCountryIds()) {
            Country country = countryService.getCountryById(countryId).orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + countryId));
            countries.add(country);
        }
        movie.setCountries(countries);
        return movieRepository.save(movie);
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }
}
