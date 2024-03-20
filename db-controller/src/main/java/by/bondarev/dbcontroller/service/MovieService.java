package by.bondarev.dbcontroller.service;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {

    private static final Logger logger = LogManager.getLogger(MovieService.class);

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

    public ResponseEntity<String> getMovieInfoFromApi(String title)  {
        String apiServiceApiUrl = apiUrl + "movie/info?title=" + title;
        ResponseEntity<String> json = sendGetRequest(apiServiceApiUrl);

        return ResponseEntity.ok(json.getBody());
    }

    public ResponseEntity<String> processApiResponse(String movieDTO) {
        ResponseEntity<String> savedMovieResponse = saveMovie(movieDTO);

        if (savedMovieResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(savedMovieResponse.getBody());
        } else {
            return ResponseEntity.status(savedMovieResponse.getStatusCode()).body("Error saving movie to database");
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
                logger.info("Request successful. Response code: {}", response.code());
            } else {
                assert response.body() != null;
                logger.error("Request failed. Response code: {}", response.code());
            }
            return new ResponseEntity<>(response.body().string(), HttpStatus.valueOf(response.code()));
        } catch (IOException e) {
            logger.error("IOException occurred during request execution", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
