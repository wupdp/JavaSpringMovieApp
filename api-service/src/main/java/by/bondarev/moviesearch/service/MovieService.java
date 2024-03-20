package by.bondarev.moviesearch.service;

import by.bondarev.moviesearch.dto.MovieApiResponse;
import by.bondarev.moviesearch.dto.MovieDTO;
import by.bondarev.moviesearch.kinopoiskapi.KinopoiskAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class MovieService {
    private final KinopoiskAPI kinopoiskAPI;
    private static final Logger logger = LogManager.getLogger(MovieService.class);

    public MovieService(KinopoiskAPI kinopoiskAPI) {
        this.kinopoiskAPI = kinopoiskAPI;
    }

    public String getMovieInfo(String title) {
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
        try {
            String listByField = kinopoiskAPI.getListOf(field);
            logger.info("Retrieved values for field: {}", field);
            return listByField;
        } catch (IOException e) {
            logger.error("Error getting list for field: {}", field, e);
            return "Error getting list. Chill, we work with this problem ;))";
        }
    }
}
