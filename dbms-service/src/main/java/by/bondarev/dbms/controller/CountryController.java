package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.CountryDTO;
import by.bondarev.dbms.service.CountryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;
    private static final Logger logger = LogManager.getLogger(CountryController.class);

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<String> getAllCountries()  {
        try {
            String response = countryService.getAllCountries();
            logger.info("Retrieved all countries successfully");
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve all countries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCountryById(@PathVariable Long id) {
        try {
            String response = countryService.getCountryById(id);
            logger.info("Retrieved country by ID: {}", id);
            if (response == null) {
                logger.warn("Country not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve country by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> saveCountry(@RequestBody CountryDTO country) {
        try {
            String response = countryService.saveCountry(country);
            logger.info("Saved country: {}", country);
            if (response == null) {
                logger.warn("Failed to save country: {}", country);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            logger.error("Failed to save country: {}", country, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        try {
            boolean success = countryService.deleteCountryById(id);
            if (!success) {
                logger.warn("Country not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            logger.info("Deleted country with ID: {}", id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            logger.error("Failed to delete country with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
