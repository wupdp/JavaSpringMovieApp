package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Person;
import by.bondarev.dbms.dto.PersonDTO;
import by.bondarev.dbms.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    public String getAllPersons() throws JsonProcessingException {
        List<Person> persons = personRepository.findAll();
        List<PersonDTO> personDTOs = persons.stream().map(Person::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(personDTOs);
    }

    public String getPersonById(Long id) throws JsonProcessingException {
        Optional<Person> person = personRepository.findById(id);
        return objectMapper.writeValueAsString(person.map(Person::toDTO).orElse(null));
    }

    public String createPerson(Person person) throws JsonProcessingException {
        Person createdPerson = personRepository.save(person);
        return objectMapper.writeValueAsString(createdPerson.toDTO());
    }

    public boolean deletePersonById(Long id) {
        personRepository.deleteById(id);
        return true;
    }
}