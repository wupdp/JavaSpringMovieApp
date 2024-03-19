package by.bondarev.dbms.service.cache;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import by.bondarev.dbms.dto.GenreDTO;

@Component
public class GenreCache {

    private final Map<Long, GenreDTO> cache = new HashMap<>();

    public void put(Long id, GenreDTO genreDTO) {
        cache.put(id, genreDTO);
    }

    public GenreDTO get(Long id) {
        return cache.get(id);
    }

    public void evict(Long id) {
        cache.remove(id);
    }
}
