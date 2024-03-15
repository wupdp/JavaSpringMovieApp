package by.bondarev.dbcontroller.service;

import by.bondarev.dbcontroller.dto.MovieDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {

    private final OkHttpClient client;

    public MovieService() {
        this.client = new OkHttpClient();
    }

    public ResponseEntity<?> getMovieInfoFromDatabase(String title) {
        String databaseApiUrl = "http://dbms-service:8080/movies/" + title;
        return sendGetRequest(databaseApiUrl);
    }

    public ResponseEntity<?> getMovieInfoFromApi(String title) {
        String apiServiceApiUrl = "http://api-service:8081/movie/info?title=" + title;
        return sendGetRequest(apiServiceApiUrl);
    }

    public ResponseEntity<?> processApiResponse(MovieDTO movieDTO) {
        ResponseEntity<?> savedMovieResponse = saveMovie(movieDTO);

        if (savedMovieResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(savedMovieResponse.getBody());
        } else {
            return ResponseEntity.status(savedMovieResponse.getStatusCode()).body("Error saving movie to database");
        }
    }

    public ResponseEntity<?> saveMovie(MovieDTO movieDTO) {
        String databaseApiUrl = "http://dbms-service:8080/movie";
        return sendPostRequest(databaseApiUrl, movieDTO);
    }

    private ResponseEntity<?> sendGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return executeRequest(request);
    }

    private ResponseEntity<?> sendPostRequest(String url, MovieDTO requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            return executeRequest(request);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> executeRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new ResponseEntity<>(response.body().string(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
