package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Person;
import by.bondarev.dbms.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    public String getAllPersonsAsJSON() throws JsonProcessingException {
        List<Person> persons = personRepository.findAll();
        return objectMapper.writeValueAsString(persons);
    }

    public String getPersonByIdAsJSON(Long id) throws JsonProcessingException {
        Optional<Person> person = personRepository.findById(id);
        return person.map(p -> {
            try {
                return objectMapper.writeValueAsString(p);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).orElse(null);
    }

    public String createPersonAsJSON(Person person) throws JsonProcessingException {
        Person createdPerson = personRepository.save(person);
        return objectMapper.writeValueAsString(createdPerson);
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }
}
