package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Person;
import by.bondarev.dbms.dto.PersonDTO;
import by.bondarev.dbms.repository.PersonRepository;
import by.bondarev.dbms.service.cache.RedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;
    private final EntityIdUpdater entityIdUpdater;
    private final RedisCache redisCache;

    @Autowired
    public PersonService(PersonRepository personRepository, ObjectMapper objectMapper, EntityIdUpdater entityIdUpdater, RedisCache redisCache) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
        this.entityIdUpdater = entityIdUpdater;
        this.redisCache = redisCache;
    }

    public String getAllPersons() throws JsonProcessingException {
        List<Person> persons = personRepository.findAll();
        List<PersonDTO> personDTOs = persons.stream().map(Person::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(personDTOs);
    }

    public String getPersonById(Long id) throws JsonProcessingException {

        PersonDTO cachedPerson = (PersonDTO) redisCache.get("person_" + id);
        if (cachedPerson != null) {
            return objectMapper.writeValueAsString(cachedPerson);
        }

        Optional<Person> person = personRepository.findById(id);
        return objectMapper.writeValueAsString(person.map(personEntity -> {
            PersonDTO personDTO = personEntity.toDTO();
            redisCache.put("person_" + id, personDTO, 1, TimeUnit.MINUTES);
            return personDTO;
        }).orElse(null));
    }

    public String savePerson(PersonDTO personDTO) throws JsonProcessingException {
        Optional<Long> existingIdOptional = personRepository.getIdByName(personDTO.getName());
        existingIdOptional.ifPresent(personDTO::setId);

        personDTO.setMovies(entityIdUpdater.updateMoviesIds(personDTO.getMovies()));

        Person savedPerson = personRepository.save(Person.fromDTO(personDTO));
        redisCache.put("person_" + savedPerson.getId(), savedPerson.toDTO(), 1, TimeUnit.MINUTES);
        return objectMapper.writeValueAsString(savedPerson.toDTO());
    }


    public boolean deletePersonById(Long id) {
        personRepository.deleteById(id);
        redisCache.delete("person_" + id);
        return true;
    }
}
