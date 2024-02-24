package by.bondarev.moviesearch.service;

import by.bondarev.moviesearch.dao.MovieDAO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {
    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public String getMovieInfo(String title) throws IOException {
        return movieDAO.getMovieInfo(title);
    }
}
