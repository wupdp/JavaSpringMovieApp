package by.bondarev.dbms.controller;

import by.bondarev.dbms.model.Person;
import by.bondarev.dbms.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getAllPersons() throws JsonProcessingException {
        return personService.getAllPersonsAsJSON();
    }

    @GetMapping("/{id}")
    public String getPersonById(@PathVariable Long id) throws JsonProcessingException {
        return personService.getPersonByIdAsJSON(id);
    }

    @PostMapping
    public String savePerson(@RequestBody Person person) throws JsonProcessingException {
        return personService.createPersonAsJSON(person);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePersonById(id);
    }
}
