package by.bondarev.controller;

import by.bondarev.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataBaseController {

    private final MovieService movieService;

    public DataBaseController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movie/info")
    public ResponseEntity<?> getMovieInfo(@RequestParam("title") String title) {
        try {
            ResponseEntity<?> movieInfo = movieService.getMovieInfo(title);
            return ResponseEntity.ok(movieInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting movie info", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
