package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import by.bondarev.dbms.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<String> getAllMovies() throws JsonProcessingException {
        String response = movieService.getAllMovies();
        logger.info("Retrieved all movies");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable Long id) throws JsonProcessingException {
        String response = movieService.getMovieById(id);
        if (response == null || response.equals("[]") || response.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        logger.info("Retrieved movie by id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byTitle/{title}")
    public ResponseEntity<String> getMovieByTitle(@PathVariable String title) throws JsonProcessingException {
        String response = movieService.getMovieByTitle(title);
        if (response == null || response.equals("[]") || response.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found with title: " + title);
        }
        logger.info("Retrieved movie by title: {}", title);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> saveMovie(@RequestBody MovieDTO movie) throws JsonProcessingException {
        String response = movieService.saveMovie(movie);
        logger.info("Saved movie: {}", movie);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byGenre/{genreName}")
    public ResponseEntity<String> findMoviesByGenre(@PathVariable String genreName) throws JsonProcessingException {
        String response = movieService.findMoviesByGenre(genreName);
        if (response == null || response.equals("[]") || response.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found with genre: " + genreName);
        }
        logger.info("Retrieved movies by genre: {}", genreName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byCountry/{countryName}")
    public ResponseEntity<String> findMoviesByCountry(@PathVariable String countryName) throws JsonProcessingException {
        String response = movieService.findMoviesByCountry(countryName);
        if (response == null || response.equals("[]") || response.isEmpty()) {
            throw new ResourceNotFoundException("Movies not found for country: " + countryName);
        }
        logger.info("Retrieved movies by country: {}", countryName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byPerson/{personName}")
    public ResponseEntity<String> findMoviesByPerson(@PathVariable String personName) throws JsonProcessingException {
        String response = movieService.findMoviesByPerson(personName);
        if (response == null || response.equals("[]") || response.isEmpty()) {
            throw new ResourceNotFoundException("Movies not found for person: " + personName);
        }
        logger.info("Retrieved movies by person: {}", personName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        boolean success = movieService.deleteMovieById(id);
        if (!success) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        logger.info("Deleted movie by id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<String>> saveBulkMovies(@RequestBody List<MovieDTO> movies) throws JsonProcessingException {
        List<String> savedMovieNames = movieService.saveBulkMovies(movies);
        logger.info("Saved bulk movies");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(savedMovieNames);
    }
}
