package by.bondarev.dbcontroller.controller;

import by.bondarev.dbcontroller.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для поиска фильмов.
 * Данный контроллер отвечает за поиск фильмов в базе данных и внешних API
 * с последующим обновлением БД.
 */
@Tag(name = "Поиск фильмов", description = "Данный контроллер отвечает за поиск фильмов в базе данных " + "и внешних API с последующим обновлением БД")
@RestController
@RequestMapping("/movie")
public class DataBaseController {
    private final MovieService movieService;

    public DataBaseController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Поиск фильма по названию", description = "Позволяет найти любой фильм по названию, если такой существует, и, при необходимости, добавить его в БД")
    @ExceptionHandler
    @GetMapping("/info")
    public ResponseEntity<String> getMovieInfo(@RequestParam("title") @Parameter(description = "Название фильма") String title) {
        ResponseEntity<String> responseFromDatabase = movieService.getMovieInfoFromDatabase("movies/byTitle/" + title);

        if (responseFromDatabase.getStatusCode().is2xxSuccessful() && responseFromDatabase.getBody() != null) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseFromDatabase.getBody());
        } else {
            ResponseEntity<String> responseFromApi = movieService.getMovieInfoFromApi(title);

            if (responseFromApi.getStatusCode().is2xxSuccessful() && responseFromApi.getBody() != null) {
                ResponseEntity<String> response = movieService.processApiResponse(responseFromApi.getBody());
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error getting movie info from API");
            }
        }
    }

    @Operation(summary = "Поиск фильмов в БД (ONLY) по фильтру: страна", description = "Возвращает все найденные фильмы по фильтру")
    @GetMapping("/byCountry")
    public ResponseEntity<String> getMoviesByCountry(@RequestParam("country") @Parameter(description = "Название страны") String country) {

        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byCountry/" + country);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @Operation(summary = "Возвращает все доступные фильмы в БД")
    @GetMapping("/all")
    public ResponseEntity<String> getAllMovies() {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies");
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @Operation(summary = "Поиск фильмов в БД (ONLY) по фильтру: жанр", description = "Возвращает все найденные фильмы по фильтру")
    @GetMapping("/byGenre")
    public ResponseEntity<String> getMoviesByGenre(@RequestParam("genre") @Parameter(description = "Название жанра") String genre) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byGenre/" + genre);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @Operation(summary = "Поиск фильмов в БД (ONLY) по фильтру: человек", description = "Возвращает все найденные фильмы по фильтру")
    @GetMapping("/byPerson")
    public ResponseEntity<String> getMoviesByPerson(@RequestParam("person") @Parameter(description = "Имя человека (полное)") String person) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byPerson/" + person);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @Operation(summary = "Поиск фильмов в БД (ONLY) по фильтру: название", description = "Возвращает все найденные фильмы по фильтру")
    @GetMapping("/byTitle")
    public ResponseEntity<String> getMovieByTitle(@RequestParam("title") @Parameter(description = "Название фильма (полное)") String title) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byTitle/" + title);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }

    @Operation(summary = "Поиск фильма в БД по идентификатору", description = "Возвращает фильм, соответсвующий заданному идентификатору в БД")
    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable @Parameter(description = "Идентификатор фильма") Long id) {
        ResponseEntity<String> movies = movieService.getMovieInfoFromDatabase("movies/byId/" + id);
        return ResponseEntity.status(movies.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(movies.getBody());
    }
}
