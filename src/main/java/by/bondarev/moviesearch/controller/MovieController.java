package by.bondarev.moviesearch.controller;

import by.bondarev.moviesearch.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movie/info")
    public ResponseEntity<String> getMovieInfo(@RequestParam("title") String title) {
        try {
            String movieInfo = movieService.getMovieInfo(title);
            return ResponseEntity.ok(movieInfo);
        } catch (IOException e) {
            return new ResponseEntity<>("Error getting movie info", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {

    }
}