package by.bondarev.moviesearch.service;

import by.bondarev.moviesearch.dto.MovieApiResponse;
import by.bondarev.moviesearch.dto.MovieDTO;
import by.bondarev.moviesearch.kinopoiskapi.KinopoiskAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final KinopoiskAPI kinopoiskAPI;

    private final RequestCounterService requestCounterService;
    private static final Logger logger = LogManager.getLogger(MovieService.class);

    public MovieService(KinopoiskAPI kinopoiskAPI, RequestCounterService requestCounterService) {
        this.kinopoiskAPI = kinopoiskAPI;
        this.requestCounterService = requestCounterService;
    }

    public String getMovieInfo(String title) {
        requestCounterService.incrementRequestCounter();
        try {
            String json = kinopoiskAPI.searchMovieByName(title);
            ObjectMapper mapper = new ObjectMapper();
            MovieApiResponse movieApiResponse = mapper.readValue(json, MovieApiResponse.class);
            MovieDTO movieDTO = MovieDTO.fromApiResponse(movieApiResponse);
            logger.info("Retrieved movie info for title: {}", title);
            return movieDTO.toJSON();
        } catch (IOException e) {
            logger.error("Error getting movie info for title: {}", title, e);
            return "Error getting movie info";
        }
    }

    public String getRandomAnime() {
        requestCounterService.incrementRequestCounter();
        try {
            String animeInfo = kinopoiskAPI.searchRandomAnime();
            logger.info("Retrieved random anime");
            return animeInfo;
        } catch (IOException e) {
            logger.error("Error getting random anime", e);
            return "Error getting anime. Chill, we work with this problem ;))";
        }
    }

    public String getRandomMovie() {
        requestCounterService.incrementRequestCounter();
        try {
            String movieInfo = kinopoiskAPI.searchRandomMovie();
            logger.info("Retrieved random movie");
            return movieInfo;
        } catch (IOException e) {
            logger.error("Error getting random movie", e);
            return "Error getting movie. Chill, we work with this problem ;))";
        }
    }

    public String getPossibleValuesByField(String field) {
        requestCounterService.incrementRequestCounter();
        try {
            String listByField = kinopoiskAPI.getListOf(field);
            logger.info("Retrieved values for field: {}", field);
            return listByField;
        } catch (IOException e) {
            logger.error("Error getting list for field: {}", field, e);
            return "Error getting list. Chill, we work with this problem ;))";
        }
    }

    public List<MovieDTO> getMovies(int n, String title) {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            for (int i = 0; i < n; i++) {
                requestCounterService.incrementRequestCounter();
                String movieByName = kinopoiskAPI.searchMovieByName(title + " " + (char)('1' + i));
                MovieApiResponse movieApiResponse = objectMapper.readValue(movieByName, MovieApiResponse.class);
                MovieDTO movieDTO = MovieDTO.fromApiResponse(movieApiResponse);
                movieDTOS.add(movieDTO);
            }
        } catch (IOException e) {
            logger.error("Error getting movies", e);
            return null;
        }
        return movieDTOS;
    }
}
