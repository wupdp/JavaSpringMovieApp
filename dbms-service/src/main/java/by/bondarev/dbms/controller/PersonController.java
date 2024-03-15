package by.bondarev.dbms.controller;

import by.bondarev.dbms.dto.PersonDTO;
import by.bondarev.dbms.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final OkHttpClient client;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
        this.client = new OkHttpClient();
    }

    @GetMapping
    public ResponseEntity<String> getAllPersons() throws JsonProcessingException {
        String response = personService.getAllPersons();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPersonById(@PathVariable Long id) throws JsonProcessingException {
        String response = personService.getPersonById(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<String> savePerson(@RequestBody PersonDTO person) throws JsonProcessingException {
        String response = personService.savePerson(person);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        boolean success = personService.deletePersonById(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
