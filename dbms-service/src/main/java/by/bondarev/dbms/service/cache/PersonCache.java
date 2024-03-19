package by.bondarev.dbms.service.cache;

import by.bondarev.dbms.dto.PersonDTO;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class PersonCache {

    private final Map<Long, PersonDTO> cache = new HashMap<>();

    public void put(Long id, PersonDTO personDTO) {
        cache.put(id, personDTO);
    }

    public PersonDTO get(Long id) {
        return cache.get(id);
    }

    public void evict(Long id) {
        cache.remove(id);
    }
}
