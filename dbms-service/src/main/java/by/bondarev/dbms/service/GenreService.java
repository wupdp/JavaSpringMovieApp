package by.bondarev.dbms.service;

import by.bondarev.dbms.dto.GenreDTO;
import by.bondarev.dbms.model.Genre;
import by.bondarev.dbms.repository.GenreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final EntityIdUpdater entityIdUpdater;

    private final ObjectMapper objectMapper;

    @Autowired
    public GenreService(GenreRepository genreRepository, EntityIdUpdater entityIdUpdater, ObjectMapper objectMapper) {
        this.genreRepository = genreRepository;
        this.entityIdUpdater = entityIdUpdater;
        this.objectMapper = objectMapper;
    }

    public String getAllGenres() throws JsonProcessingException {
        List<Genre> genres = genreRepository.findAll();
        List<GenreDTO> genreDTOs = genres.stream().map(Genre::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(genreDTOs);
    }

    public String getGenreById(Long id) throws JsonProcessingException {
        Optional<Genre> genre = genreRepository.findById(id);
        return objectMapper.writeValueAsString(genre.map(Genre::toDTO).orElse(null));
    }

    public String saveGenre(GenreDTO genreDTO) throws JsonProcessingException {
        Optional<Long> existingIdOptional = genreRepository.getIdByName(genreDTO.getName());
        existingIdOptional.ifPresent(genreDTO::setId);

        genreDTO.setMovies(entityIdUpdater.updateMoviesIds(genreDTO.getMovies()));

        Genre savedGenre = genreRepository.save(Genre.fromDTO(genreDTO));

        return objectMapper.writeValueAsString(savedGenre.toDTO());
    }

    public boolean deleteGenreById(Long id) {
        genreRepository.deleteById(id);
        return true;
    }
}
