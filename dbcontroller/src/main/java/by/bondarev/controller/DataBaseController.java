package by.bondarev.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataBaseController {

    private final MovieService movieService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final PersonService personService;

    public DataBaseController(MovieService movieService, CountryService countryService, GenreService genreService, PersonService personService) {
        this.movieService = movieService;
        this.countryService = countryService;
        this.genreService = genreService;
        this.personService = personService;
    }

    // Эндпоинты для работы с фильмами
    @GetMapping("/movie/info")
    public ResponseEntity<String> getMovieInfo(@RequestParam("title") String title) {
        try {
            String movieInfo = movieService.getMovieInfo(title);
            return ResponseEntity.ok(movieInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting movie info", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movie/random/anime")
    public ResponseEntity<String> getRandomAnime() {
        try {
            String animeInfo = movieService.getRandomAnime();
            return ResponseEntity.ok(animeInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting anime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movie/random")
    public ResponseEntity<String> getRandomMovie() {
        try {
            String movieInfo = movieService.getRandomMovie();
            return ResponseEntity.ok(movieInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting movie", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/values")
    public ResponseEntity<String> getValuesByField(@RequestParam("field") String field) {
        try {
            String listByField = movieService.getPossibleValuesByField(field);
            return ResponseEntity.ok(listByField);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting list", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Эндпоинты для работы со странами
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        try {
            List<Country> countries = countryService.getAllCountries();
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        try {
            Optional<Country> country = countryService.getCountryById(id);
            return country.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/countries")
    public ResponseEntity<Country> saveCountry(@RequestBody Country country) {
        try {
            Country savedCountry = countryService.saveCountry(country);
            return ResponseEntity.ok(savedCountry);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        try {
            countryService.deleteCountryById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getAllGenres() {
        try {
            List<Genre> genres = genreService.getAllGenres();
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        try {
            Optional<Genre> genre = genreService.getGenreById(id);
            return genre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/genres")
    public ResponseEntity<Genre> saveGenre(@RequestBody Genre genre) {
        try {
            Genre savedGenre = genreService.saveGenre(genre);
            return ResponseEntity.ok(savedGenre);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        try {
            genreService.deleteGenreById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
