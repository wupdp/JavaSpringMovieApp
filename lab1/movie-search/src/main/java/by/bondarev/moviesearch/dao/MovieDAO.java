package by.bondarev.moviesearch.dao;

import by.bondarev.moviesearch.kinopoiskAPI.KinopoiskAPI;

import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MovieDAO {
    private final KinopoiskAPI kinopoiskAPI;

    public MovieDAO(KinopoiskAPI kinopoiskAPI) {
        this.kinopoiskAPI = kinopoiskAPI;
    }

    public String getMovieInfo(String title) throws IOException {
        return kinopoiskAPI.searchMovie(title).string();
    }
}
