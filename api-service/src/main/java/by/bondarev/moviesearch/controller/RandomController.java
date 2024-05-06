package by.bondarev.moviesearch.controller;

import by.bondarev.moviesearch.dto.MovieDTO;
import by.bondarev.moviesearch.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Tag(name="Сервис внешнего API", description="Данный сервис предоставляет методы для работы с внешним API кинопоиска")
@RestController
public class RandomController {
    private final MovieService movieService;

    public RandomController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(
            summary = "Случайное аниме",
            description = "Позволяет получить случайное аниме для вашего вечера и его описание ⍝◜ᐢ•\uD83D\uDC3D•ᐢ◝⍝"
    )
    @GetMapping("/movie/random/anime")
    public ResponseEntity<String> getRandomAnime() {
        String animeInfo = movieService.getRandomAnime();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(animeInfo);
    }

    @Operation(
            summary = "Случайный фильм",
            description = "Позволяет получить случайный фильм, если вы захотите что-то \"посмотреть\" с вашей второй половинкой \uD83D\uDE09\uD83D\uDE09"
    )
    @GetMapping("/movie/random")
    public ResponseEntity<String> getRandomMovie() {
        String movieInfo = movieService.getRandomMovie();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(movieInfo);
    }

    @Operation(
            summary = "BULK (BONK)",
            description = "Получить части фильмов и сохранить их пачкой в базе данных"
    )
    @PostMapping("/movie/bulk")
    public ResponseEntity<String> getMoviesAndSaveBulk(@RequestParam("n") int n, @RequestParam("title") String title) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            List<MovieDTO> movies = movieService.getMovies(n, title);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<MovieDTO>> requestEntity = new HttpEntity<>(movies, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "http://dbms-service:8080/movies/bulk",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            return ResponseEntity.ok().body(responseEntity.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

