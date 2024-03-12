package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.model.Movie;
import by.bondarev.dbms.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Optional<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }
    @GetMapping("/{title}")
    public List<Movie> getMovieByTitle(@PathVariable String title) {
        return movieService.getMovieByTitle(title);
    }

    @PostMapping
    public Movie saveMovie(@RequestBody MovieDTO movie) {
        return movieService.saveMovie(movie);
    }

    @GetMapping("/byGenre/{genreName}")
    public List<Movie> findMoviesByGenre(@PathVariable String genreName) {
        return movieService.findMoviesByGenre(genreName);
    }

    @GetMapping("/byCountry/{countryName}")
    public List<Movie> findMoviesByCountry(@PathVariable String countryName) {
        return movieService.findMoviesByCountry(countryName);
    }

    @GetMapping("/byPerson/{personName}")
    public List<Movie> findMoviesByPerson(@PathVariable String personName) {
        return movieService.findMoviesByPerson(personName);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovieById(id);
    }
}
