package by.bondarev.dbms.controller;

import by.bondarev.dbms.model.Genre;
import by.bondarev.dbms.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public Genre saveGenre(@RequestBody Genre genre) {
        return genreService.saveGenre(genre);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenreById(id);
    }
}

