package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Movie;
import by.bondarev.dbms.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }
}
