package by.bondarev.moviesearch.service;

import by.bondarev.moviesearch.kinopoiskapi.KinopoiskAPI;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {
    private final KinopoiskAPI kinopoiskAPI;

    public MovieService(KinopoiskAPI kinopoiskAPI) {
        this.kinopoiskAPI = kinopoiskAPI;
    }

    public String getMovieInfo(String title) throws IOException {
        return kinopoiskAPI.searchMovieByName(title);
        //TODO little json
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
