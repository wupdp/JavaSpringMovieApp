package by.bondarev.service;

import by.bondarev.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {

    private final RestTemplate restTemplate;
    private final String databaseServiceUrl = "http://dbms-service:8080"; // URL микросервиса СУБД
    private final String apiServiceUrl = "http://api-service:8081"; // URL микросервиса API

    @Autowired
    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> getMovieInfo(String title) {
        String databaseApiUrl = databaseServiceUrl + "/movies/" + title;
        ResponseEntity<MovieDTO> responseFromDatabase = restTemplate.getForEntity(databaseApiUrl, MovieDTO.class);

        if (responseFromDatabase.getStatusCode() == HttpStatus.OK && responseFromDatabase.getBody() != null) {
            return responseFromDatabase;
        } else {
            String apiServiceApiUrl = apiServiceUrl + "/movie/info?title=" + title;
            ResponseEntity<MovieDTO> responseFromApi = restTemplate.getForEntity(apiServiceApiUrl, MovieDTO.class);

            if (responseFromApi.getStatusCode() == HttpStatus.OK && responseFromApi.getBody() != null) {
                ResponseEntity<MovieDTO> savedMovieResponse = saveMovie(responseFromApi.getBody());

                if (savedMovieResponse.getStatusCode() == HttpStatus.CREATED) {
                    return ResponseEntity.ok(savedMovieResponse.getBody());
                } else {
                    return new ResponseEntity<>("Error saving movie to database", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Error getting movie info from API", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<MovieDTO> saveMovie(MovieDTO movieDTO) {
            String databaseApiUrl = databaseServiceUrl + "/movie";
        return restTemplate.postForEntity(databaseApiUrl, movieDTO, MovieDTO.class);
    }
}