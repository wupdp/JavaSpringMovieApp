package by.bondarev.moviesearch.kinopoiskAPI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KinopoiskAPI {

   /* @Value("${kinopoisk.apikey}")
    private String apiKey;*/
    private static final String API_KEY = "9AXTNXV-6Z7M2EQ-K20RAMR-4CR1ZAE";

    private static final String API_URL = "https://api.kinopoisk.dev/v1.4/movie/search";

    private final OkHttpClient client;

    public KinopoiskAPI() {
        this.client = new OkHttpClient();
    }

    public ResponseBody searchMovie(String query) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "?page=1&limit=10&query=" + query)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", API_KEY)
                .build();

        return client.newCall(request).execute().body();
    }
}
