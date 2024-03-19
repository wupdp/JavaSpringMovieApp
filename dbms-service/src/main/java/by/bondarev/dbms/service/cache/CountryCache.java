package by.bondarev.dbms.service.cache;

import by.bondarev.dbms.dto.CountryDTO;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class CountryCache {

    private final Map<Long, CountryDTO> cache = new HashMap<>();

    public void put(Long id, CountryDTO countryDTO) {
        cache.put(id, countryDTO);
    }

    public CountryDTO get(Long id) {
        return cache.get(id);
    }

    public void evict(Long id) {
        cache.remove(id);
    }
}
