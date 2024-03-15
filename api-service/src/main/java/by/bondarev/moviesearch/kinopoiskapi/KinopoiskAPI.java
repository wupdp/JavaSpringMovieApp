package by.bondarev.moviesearch.kinopoiskapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class KinopoiskAPI {

    @Value("${kinopoisk.apikey}")
    private String apiKey;

    @Value("${kinopoisk.url}")
    private String apiUrl;

    private final OkHttpClient client;

    public KinopoiskAPI() {
        this.client = new OkHttpClient();
    }

    private String makeRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", apiKey)
                .build();

        return Objects.requireNonNull(client.newCall(request).execute().body()).string();
    }
    //Movie requests
    public String searchMovieByName(String query) throws IOException {
        String url = apiUrl + "movie/search?page=1&limit=1&query=" + query;
        return makeRequest(url);
    }

    public String searchRandomAnime() throws IOException {
        String url = apiUrl + "movie/random?notNullFields=name&type=anime";
        return makeRequest(url);
    }
    public String searchRandomMovie() throws IOException {
        String url = apiUrl + "movie/random?notNullFields=name";
        return makeRequest(url);
    }

    //List info
    public String getListOf(String field) throws IOException {
        String url = "https://api.kinopoisk.dev/v1/movie/possible-values-by-field?field=" + field;
        return makeRequest(url);
    }
}