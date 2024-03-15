package by.bondarev.dbcontroller.controller;

import by.bondarev.dbcontroller.dto.MovieDTO;
import by.bondarev.dbcontroller.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataBaseController {
    private final MovieService movieService;

    public DataBaseController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movie/info")
    public ResponseEntity<?> getMovieInfo(@RequestParam("title") String title) {
        ResponseEntity<?> responseFromDatabase = movieService.getMovieInfoFromDatabase(title);

        if (responseFromDatabase.getStatusCode().is2xxSuccessful() && responseFromDatabase.getBody() != null) {
            return responseFromDatabase;
        } else {
            ResponseEntity<?> responseFromApi = movieService.getMovieInfoFromApi(title);

            if (responseFromApi.getStatusCode().is2xxSuccessful() && responseFromApi.getBody() != null) {
                return movieService.processApiResponse((MovieDTO) responseFromApi.getBody());
            } else {
                return ResponseEntity.status(responseFromApi.getStatusCode()).body("Error getting movie info from API");
            }
        }
    }
}
