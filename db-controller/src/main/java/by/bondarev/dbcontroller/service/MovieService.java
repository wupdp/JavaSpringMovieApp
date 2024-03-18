package by.bondarev.dbcontroller.service;

import by.bondarev.dbcontroller.dto.MovieDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {

    private final OkHttpClient client;

    @Value("${dbms.url}")
    private String dbmsUrl;

    @Value("${api.url}")
    private String apiUrl;

    public MovieService() {
        this.client = new OkHttpClient();
    }

    public ResponseEntity<String> getMovieInfoFromDatabase(String requestPath) {
        String databaseApiUrl = dbmsUrl + requestPath;
        return sendGetRequest(databaseApiUrl);
    }

    public ResponseEntity<String> getMovieInfoFromApi(String title) throws JsonProcessingException {
        String apiServiceApiUrl = apiUrl + "movie/info?title=" + title;
        ResponseEntity<String> json = sendGetRequest(apiServiceApiUrl);

        return ResponseEntity.ok(json.getBody());
    }

    public ResponseEntity<String> processApiResponse(String movieDTO) {
        ResponseEntity<String> savedMovieResponse = saveMovie(movieDTO);

        if (savedMovieResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(savedMovieResponse.getBody());
        } else {
            return ResponseEntity.status(savedMovieResponse.getStatusCode()).body("Errorrr saving movie to database");
        }
    }

    public ResponseEntity<String> saveMovie(String movieDTO) {
        String databaseApiUrl = dbmsUrl + "movies";
        return sendPostRequest(databaseApiUrl, movieDTO);
    }

    private ResponseEntity<String> sendGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return executeRequest(request);
    }

    private ResponseEntity<String> sendPostRequest(String url, String requestBody) {
        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return executeRequest(request);
    }

    private ResponseEntity<String> executeRequest(Request request) {
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
