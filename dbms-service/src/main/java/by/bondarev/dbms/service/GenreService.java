package by.bondarev.dbms.service;

import by.bondarev.dbms.dto.GenreDTO;
import by.bondarev.dbms.model.Genre;
import by.bondarev.dbms.repository.GenreRepository;
import by.bondarev.dbms.service.cache.GenreCache;
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

    private final GenreCache genreCache;
    private final ObjectMapper objectMapper;

    @Autowired
    public GenreService(GenreRepository genreRepository, EntityIdUpdater entityIdUpdater, ObjectMapper objectMapper, GenreCache genreCache) {
        this.genreRepository = genreRepository;
        this.entityIdUpdater = entityIdUpdater;
        this.objectMapper = objectMapper;
        this.genreCache = genreCache;
    }

    public String getAllGenres() throws JsonProcessingException {
        List<Genre> genres = genreRepository.findAll();
        List<GenreDTO> genreDTOs = genres.stream().map(Genre::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(genreDTOs);
    }

    public String getGenreById(Long id) throws JsonProcessingException {
        GenreDTO cachedGenre = genreCache.get(id);
        if (cachedGenre != null) {
            return objectMapper.writeValueAsString(cachedGenre);
        }

        Optional<Genre> genre = genreRepository.findById(id);
        return objectMapper.writeValueAsString(genre.map(genreEntity -> {
            GenreDTO genreDTO = genreEntity.toDTO();
            genreCache.put(id, genreDTO); // Добавляем в кэш
            return genreDTO;
        }).orElse(null));
    }

    public String saveGenre(GenreDTO genreDTO) throws JsonProcessingException {
        Optional<Long> existingIdOptional = genreRepository.getIdByName(genreDTO.getName());
        existingIdOptional.ifPresent(genreDTO::setId);

        genreDTO.setMovies(entityIdUpdater.updateMoviesIds(genreDTO.getMovies()));

        Genre savedGenre = genreRepository.save(Genre.fromDTO(genreDTO));

        genreCache.put(savedGenre.getId(), savedGenre.toDTO());
        return objectMapper.writeValueAsString(savedGenre.toDTO());
    }

    public boolean deleteGenreById(Long id) {
        genreRepository.deleteById(id);
        genreCache.evict(id);
        return true;
    }
}
