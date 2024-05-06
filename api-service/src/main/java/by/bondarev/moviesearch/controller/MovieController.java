package by.bondarev.moviesearch.controller;

import by.bondarev.moviesearch.service.MovieService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movie/info")
    public ResponseEntity<String> getMovieInfo(@RequestParam("title") String title) {
        String movieInfo = movieService.getMovieInfo(title);
        return ResponseEntity.ok(movieInfo);
    }

    @GetMapping("/values")
    public ResponseEntity<String> getValuesByField(@RequestParam("field") String field) {
        String listByField = movieService.getPossibleValuesByField(field);
        return ResponseEntity.ok(listByField);
    }
}
