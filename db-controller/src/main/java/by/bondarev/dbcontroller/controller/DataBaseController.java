package by.bondarev.dbcontroller.controller;

import by.bondarev.dbcontroller.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class DataBaseController {
    private final MovieService movieService;

    public DataBaseController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/info")
    public ResponseEntity<String> getMovieInfo(@RequestParam("title") String title) throws JsonProcessingException {
        ResponseEntity<String> responseFromDatabase = movieService.getMovieInfoFromDatabase("movies/byTitle/" + title);

        if (responseFromDatabase.getStatusCode().is2xxSuccessful()
                && responseFromDatabase.getBody() != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseFromDatabase
                            .getBody());
        } else {
            ResponseEntity<String> responseFromApi = movieService.getMovieInfoFromApi(title);

            if (responseFromApi.getStatusCode().is2xxSuccessful()
                    && responseFromApi.getBody() != null) {
                ResponseEntity<String> response = movieService
                        .processApiResponse(responseFromApi.getBody());
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseFromApi
                                .getBody());
            } else {
                return ResponseEntity
                        .status(responseFromApi
                                .getStatusCode())
                        .body("Error getting movie info from API");
            }
        }
    }

    @GetMapping("/byCountry")
    public ResponseEntity<String> getMoviesByCountry(@RequestParam("country") String country) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(movieService
                        .getMovieInfoFromDatabase("movies/byCountry/" + country)
                        .getBody());
    }

    @GetMapping("/byGenre")
    public ResponseEntity<String> getMoviesByGenre(@RequestParam("genre") String genre) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(movieService
                        .getMovieInfoFromDatabase("movies/byGenre/" + genre)
                        .getBody());
    }

    @GetMapping("/byPerson")
    public ResponseEntity<String> getMoviesByPerson(@RequestParam("person") String person) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(movieService
                        .getMovieInfoFromDatabase("movies/byPerson/" + person)
                        .getBody());
    }

    @GetMapping("/byTitle")
    public ResponseEntity<String> getMovieByTitle(@RequestParam("title") String title) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(movieService
                        .getMovieInfoFromDatabase("movies/byTitle/" + title)
                        .getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(movieService
                        .getMovieInfoFromDatabase("movies/" + id)
                        .getBody());
    }
}
