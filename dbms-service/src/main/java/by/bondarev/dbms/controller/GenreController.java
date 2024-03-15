package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.GenreDTO;
import by.bondarev.dbms.service.GenreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final OkHttpClient client;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
        this.client = new OkHttpClient();
    }

    @GetMapping
    public ResponseEntity<String> getAllGenres() throws JsonProcessingException {
        String response = genreService.getAllGenres();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getGenreById(@PathVariable Long id) throws JsonProcessingException {
        String response = genreService.getGenreById(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> saveGenre(@RequestBody GenreDTO genre) throws JsonProcessingException {
        String response = genreService.saveGenre(genre);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        boolean success = genreService.deleteGenreById(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
