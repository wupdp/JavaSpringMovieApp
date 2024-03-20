package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.PersonDTO;
import by.bondarev.dbms.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<String> getAllPersons() throws JsonProcessingException {
        String response = personService.getAllPersons();
        if (response == null) {
            logger.error("Error retrieving all persons: Response is null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        logger.info("Retrieved all persons successfully");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPersonById(@PathVariable Long id) throws JsonProcessingException {
        String response = personService.getPersonById(id);
        if (response == null) {
            logger.warn("Person not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Retrieved person by id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> savePerson(@RequestBody PersonDTO person) throws JsonProcessingException {
        String response = personService.savePerson(person);
        if (response == null) {
            logger.error("Error saving person: Request body is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        logger.info("Saved person successfully: {}", person);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        boolean success = personService.deletePersonById(id);
        if (!success) {
            logger.warn("Person not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Deleted person by id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
