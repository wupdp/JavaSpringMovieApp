package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.GenreDTO;
import by.bondarev.dbms.service.GenreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private static final Logger logger = LogManager.getLogger(GenreController.class);

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<String> getAllGenres() throws JsonProcessingException {
        logger.info("Received request to get all genres");
        String response = genreService.getAllGenres();
        if (response == null) {
            logger.error("Failed to retrieve all genres");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        logger.info("Successfully retrieved all genres");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getGenreById(@PathVariable Long id) throws JsonProcessingException {
        logger.info("Received request to get genre by id: {}", id);
        String response = genreService.getGenreById(id);
        if (response == null) {
            logger.warn("Genre not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Successfully retrieved genre with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> saveGenre(@RequestBody GenreDTO genre) throws JsonProcessingException {
        logger.info("Received request to save genre: {}", genre);
        String response = genreService.saveGenre(genre);
        if (response == null) {
            logger.error("Failed to save genre: {}", genre);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        logger.info("Successfully saved genre: {}", genre);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        logger.info("Received request to delete genre with id: {}", id);
        boolean success = genreService.deleteGenreById(id);
        if (!success) {
            logger.warn("Genre not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Successfully deleted genre with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
