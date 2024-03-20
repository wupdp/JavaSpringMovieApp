package by.bondarev.dbms.service;

import by.bondarev.dbms.dto.GenreDTO;
import by.bondarev.dbms.model.Genre;
import by.bondarev.dbms.repository.GenreRepository;
import by.bondarev.dbms.service.cache.RedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final EntityIdUpdater entityIdUpdater;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    @Autowired
    public GenreService(GenreRepository genreRepository, EntityIdUpdater entityIdUpdater, ObjectMapper objectMapper, RedisCache redisCache) {
        this.genreRepository = genreRepository;
        this.entityIdUpdater = entityIdUpdater;
        this.objectMapper = objectMapper;
        this.redisCache = redisCache;
    }

    public String getAllGenres() throws JsonProcessingException {
        List<Genre> genres = genreRepository.findAll();
        List<GenreDTO> genreDTOs = genres.stream().map(Genre::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(genreDTOs);
    }

    public String getGenreById(Long id) throws JsonProcessingException {
        String cachedGenre = (String) redisCache.get("genre_" + id);
        if (cachedGenre != null) {
            logger.info("Retrieved genre from cache by id: {}", id);
            return cachedGenre;
        }

        Optional<Genre> genre = genreRepository.findById(id);
        return objectMapper.writeValueAsString(genre.map(genreEntity -> {
            GenreDTO genreDTO = genreEntity.toDTO();
            redisCache.put("genre_" + id, genreDTO, 1, TimeUnit.MINUTES); // Добавляем в кэш
            logger.info("Retrieved genre by id: {}", id);
            return genreDTO;
        }).orElse(null));
    }

    public String saveGenre(GenreDTO genreDTO) throws JsonProcessingException {
        Optional<Long> existingIdOptional = genreRepository.getIdByName(genreDTO.getName());
        existingIdOptional.ifPresent(genreDTO::setId);

        genreDTO.setMovies(entityIdUpdater.updateMoviesIds(genreDTO.getMovies()));

        Genre savedGenre = genreRepository.save(Genre.fromDTO(genreDTO));

        redisCache.put("genre_" + savedGenre.getId(), savedGenre.toDTO(), 1, TimeUnit.MINUTES);
        logger.info("Saved genre in cache: {}", savedGenre);

        return objectMapper.writeValueAsString(savedGenre.toDTO());
    }

    public boolean deleteGenreById(Long id) {
        genreRepository.deleteById(id);
        redisCache.delete("genre_" + id);
        return true;
    }
}
