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
    public List<Movie> getMovieByTitle(String title) {
        return movieRepository.findByName(title);
    }

    public Movie saveMovie(MovieDTO movieDTO) {
        return movieRepository.save(Movie.fromDTO(movieDTO));
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }
}
