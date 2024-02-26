package by.bondarev.moviesearch.service;

import by.bondarev.moviesearch.kinopoiskAPI.KinopoiskAPI;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {
    private final KinopoiskAPI kinopoiskAPI;

    public MovieService(KinopoiskAPI kinopoiskAPI) {
        this.kinopoiskAPI = kinopoiskAPI;
    }

    public String getMovieInfo(String title) throws IOException {
        return kinopoiskAPI.searchMovie(title);
    }
}
