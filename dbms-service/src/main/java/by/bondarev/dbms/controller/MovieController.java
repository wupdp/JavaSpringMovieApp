package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final OkHttpClient client;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
        this.client = new OkHttpClient();
    }

    @GetMapping
    public ResponseEntity<String> getAllMovies() throws JsonProcessingException {
        String response = movieService.getAllMovies();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable Long id) throws JsonProcessingException {
        String response = movieService.getMovieById(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byTitle/{title}")
    public ResponseEntity<String> getMovieByTitle(@PathVariable String title) throws JsonProcessingException {
        String response = movieService.getMovieByTitle(title);
        if (Objects.equals(response, "[]")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> saveMovie(@RequestBody MovieDTO movie) throws JsonProcessingException {
        String response = movieService.saveMovie(movie);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byGenre/{genreName}")
    public ResponseEntity<String> findMoviesByGenre(@PathVariable String genreName) throws JsonProcessingException {
        String response = movieService.findMoviesByGenre(genreName);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byCountry/{countryName}")
    public ResponseEntity<String> findMoviesByCountry(@PathVariable String countryName) throws JsonProcessingException {
        String response = movieService.findMoviesByCountry(countryName);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/byPerson/{personName}")
    public ResponseEntity<String> findMoviesByPerson(@PathVariable String personName) throws JsonProcessingException {
        String response = movieService.findMoviesByPerson(personName);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        boolean success = movieService.deleteMovieById(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
