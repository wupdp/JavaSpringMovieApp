package by.bondarev.moviesearch.service;

import by.bondarev.moviesearch.dto.MovieApiResponse;
import by.bondarev.moviesearch.dto.MovieDTO;
import by.bondarev.moviesearch.kinopoiskapi.KinopoiskAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {
    private final KinopoiskAPI kinopoiskAPI;

    public MovieService(KinopoiskAPI kinopoiskAPI) {
        this.kinopoiskAPI = kinopoiskAPI;
    }

    public String getMovieInfo(String title) throws IOException {
        String json = kinopoiskAPI.searchMovieByName(title);

        ObjectMapper mapper = new ObjectMapper();

        MovieApiResponse movieApiResponse = mapper.readValue(json, MovieApiResponse.class);
        MovieDTO movieDTO = MovieDTO.fromApiResponse(movieApiResponse);

        return movieDTO.toJSON();
    }

    public String getRandomAnime() throws IOException {
        return kinopoiskAPI.searchRandomAnime();
    }

    public String getRandomMovie() throws IOException {
        return kinopoiskAPI.searchRandomMovie();
    }

    public String getPossibleValuesByField(String field) throws IOException {
        return kinopoiskAPI.getListOf(field);
    }

}
