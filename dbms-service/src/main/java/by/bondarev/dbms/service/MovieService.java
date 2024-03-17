package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Movie;
import by.bondarev.dbms.dto.MovieDTO;
import by.bondarev.dbms.repository.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;
    private final EntityIdUpdater entityIdUpdater;

    @Autowired
    public MovieService(MovieRepository movieRepository, ObjectMapper objectMapper, EntityIdUpdater entityIdUpdater) {
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
        this.entityIdUpdater = entityIdUpdater;
    }

    public String findMoviesByGenre(String genreName) throws JsonProcessingException {
        List<Movie> movies = movieRepository.findByGenresName(genreName);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(movieDTOs);
    }

    public String findMoviesByCountry(String countryName) throws JsonProcessingException {
        List<Movie> movies = movieRepository.findByCountriesName(countryName);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(movieDTOs);
    }

    public String findMoviesByPerson(String personName) throws JsonProcessingException {
        List<Movie> movies = movieRepository.findByPersonsName(personName);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(movieDTOs);
    }

    public String getAllMovies() throws JsonProcessingException {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(movieDTOs);
    }

    public String getMovieById(Long id) throws JsonProcessingException {
        Optional<Movie> movie = movieRepository.findById(id);
        return objectMapper.writeValueAsString(movie.map(Movie::toDTO).orElse(null));
    }

    public String getMovieByTitle(String title) throws JsonProcessingException {
        List<Movie> movies = movieRepository.findByName(title);
        List<MovieDTO> movieDTOs = movies.stream().map(Movie::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(movieDTOs);
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

        return objectMapper.writeValueAsString(movie.toDTO());
    }

    public boolean deleteMovieById(Long id) {
        movieRepository.deleteById(id);
        return true;
    }
}
