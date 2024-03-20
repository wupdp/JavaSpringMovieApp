package by.bondarev.dbcontroller.controller;

import by.bondarev.dbcontroller.service.MovieService;
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
    public ResponseEntity<String> getMovieInfo(@RequestParam("title") String title) {
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
                        .body(response
                                .getBody());
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Error getting movie info from API");
            }
        }
    }

    @GetMapping("/byCountry")
    public ResponseEntity<String> getMoviesByCountry(@RequestParam("country") String country) {

        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byCountry/" + country);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllMovies() {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies");
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @GetMapping("/byGenre")
    public ResponseEntity<String> getMoviesByGenre(@RequestParam("genre") String genre) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byGenre/" + genre);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @GetMapping("/byPerson")
    public ResponseEntity<String> getMoviesByPerson(@RequestParam("person") String person) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byPerson/" + person);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @GetMapping("/byTitle")
    public ResponseEntity<String> getMovieByTitle(@RequestParam("title") String title) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byTitle/" + title);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable Long id) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byId/" + id);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }
}
