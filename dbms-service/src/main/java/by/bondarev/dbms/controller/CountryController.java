package by.bondarev.dbms.controller;

import by.bondarev.dbms.model.Country;
import by.bondarev.dbms.service.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;
    private final OkHttpClient client;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
        this.client = new OkHttpClient();
    }

    @GetMapping
    public ResponseEntity<String> getAllCountries() throws JsonProcessingException {
        String response = countryService.getAllCountries();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCountryById(@PathVariable Long id) throws JsonProcessingException {
        String response = countryService.getCountryById(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> saveCountry(@RequestBody Country country) throws JsonProcessingException {
        String response = countryService.saveCountry(country);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        boolean success = countryService.deleteCountryById(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
