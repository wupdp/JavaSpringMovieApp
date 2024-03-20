package by.bondarev.dbms.service;

import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.model.Movie;
import by.bondarev.dbms.repository.MovieRepository;
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
public class MovieService {

    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;
    private final EntityIdUpdater entityIdUpdater;
    private final RedisCache redisCache;
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    public MovieService(MovieRepository movieRepository, ObjectMapper objectMapper, EntityIdUpdater entityIdUpdater, RedisCache redisCache) {
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
        this.entityIdUpdater = entityIdUpdater;
        this.redisCache = redisCache;
    }

    public String findMoviesByGenre(String genreName) throws JsonProcessingException {
        String cachedMovies = (String) redisCache.get(genreName);
        if (cachedMovies != null) {
            logger.info("Retrieved movies from cache by genre: {}", genreName);
            return cachedMovies;
        }

        List<Movie> movies = movieRepository.findByGenresName(genreName);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(movieDTOs);
        redisCache.put(genreName, result, 1, TimeUnit.MINUTES);
        return result;
    }

    public String findMoviesByCountry(String countryName) throws JsonProcessingException {
        String cachedMovies = (String) redisCache.get(countryName);
        if (cachedMovies != null) {
            logger.info("Retrieved movies from cache by country: {}", countryName);
            return cachedMovies;
        }

        List<Movie> movies = movieRepository.findByCountriesName(countryName);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(movieDTOs);
        redisCache.put(countryName, result, 1, TimeUnit.MINUTES);
        return result;
    }

    public String findMoviesByPerson(String personName) throws JsonProcessingException {
        String cachedMovies = (String) redisCache.get(personName);
        if (cachedMovies != null) {
            logger.info("Retrieved movies from cache by person: {}", personName);
            return cachedMovies;
        }

        List<Movie> movies = movieRepository.findByPersonsName(personName);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(movieDTOs);
        redisCache.put(personName, result, 1, TimeUnit.MINUTES);
        return result;
    }

    public String getAllMovies() throws JsonProcessingException {
        String cachedMovies = (String) redisCache.get("allMovies");
        if (cachedMovies != null) {
            logger.info("Retrieved all movies from cache");
            return cachedMovies;
        }

        List<Movie> movies = movieRepository.findAll();
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(movieDTOs);
        redisCache.put("allMovies", result, 1, TimeUnit.MINUTES);
        return result;
    }

    public String getMovieById(Long id) throws JsonProcessingException {
        String cachedMovie = (String) redisCache.get("movie_" + id);
        if (cachedMovie != null) {
            logger.info("Retrieved movie from cache by id: {}", id);
            return cachedMovie;
        }

        Optional<Movie> movie = movieRepository.findById(id);
        String result = objectMapper.writeValueAsString(movie.map(Movie::toDTO).orElse(null));
        if (result != null) {
            redisCache.put("movie_" + id, result, 1, TimeUnit.MINUTES);
        }
        return result;
    }

    public String getMovieByTitle(String title) throws JsonProcessingException {
        String cachedMovies = (String) redisCache.get("movie_" + title);
        if (cachedMovies != null) {
            logger.info("Retrieved movie from cache by title: {}", title);
            return cachedMovies;
        }

        List<Movie> movies = movieRepository.findByName(title);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(movieDTOs);
        redisCache.put("movie_" + title, result, 1, TimeUnit.MINUTES);
        return result;
    }

    public String saveMovie(MovieDTO movieDTO) throws JsonProcessingException {
        Optional<Long> existingMovieIdOptional = movieRepository.getIdByName(movieDTO.getName());
        existingMovieIdOptional.ifPresent(movieDTO::setId);

        movieDTO.setPersons(entityIdUpdater.updatePersonsIds(movieDTO.getPersons()));
        movieDTO.setGenres(entityIdUpdater.updateGenresIds(movieDTO.getGenres()));
        movieDTO.setCountries(entityIdUpdater.updateCountriesIds(movieDTO.getCountries()));
        movieDTO.setStatusId(entityIdUpdater.updateStatusId(movieDTO.getStatus()));
        movieDTO.setTypeNumber(entityIdUpdater.updateTypeId(movieDTO.getType()));

        Movie movie = movieRepository.save(Movie.fromDTO(movieDTO));

        String result = objectMapper.writeValueAsString(movie.toDTO());
        if (result != null) {
            redisCache.put("movie_" + movie.getId(), result, 1, TimeUnit.MINUTES);
        }
        return result;
    }

    public boolean deleteMovieById(Long id) {
        movieRepository.deleteById(id);
        redisCache.delete("movie_" + id);
        return true;
    }
}