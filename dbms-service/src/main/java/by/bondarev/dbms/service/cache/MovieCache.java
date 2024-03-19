package by.bondarev.dbms.service.cache;

import by.bondarev.dbms.dto.MovieDTO;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MovieCache {

    private final Map<Long, MovieDTO> movieCache = new HashMap<>();

    public void put(Long id, MovieDTO movieDTO) {
        movieCache.put(id, movieDTO);
    }

    public MovieDTO get(Long id) {
        return movieCache.get(id);
    }

    public void evict(Long id) {
        movieCache.remove(id);
    }
}
