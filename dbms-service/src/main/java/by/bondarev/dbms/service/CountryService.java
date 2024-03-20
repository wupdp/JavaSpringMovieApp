package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Country;
import by.bondarev.dbms.repository.CountryRepository;
import by.bondarev.dbms.service.cache.RedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import by.bondarev.dbms.dto.CountryDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final EntityIdUpdater entityIdUpdater;
    private final ObjectMapper objectMapper;
    private final RedisCache redisCache;
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    public CountryService(CountryRepository countryRepository, EntityIdUpdater entityIdUpdater, ObjectMapper objectMapper, RedisCache redisCache) {
        this.countryRepository = countryRepository;
        this.entityIdUpdater = entityIdUpdater;
        this.objectMapper = objectMapper;
        this.redisCache = redisCache;
    }

    public String getAllCountries() throws JsonProcessingException {
        List<Country> countries = countryRepository.findAll();
        List<CountryDTO> countryDTOs = countries.stream().map(Country::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(countryDTOs);
    }

    public String getCountryById(Long id) throws JsonProcessingException {
        String cachedCountry = (String) redisCache.get("country_" + id);
        if (cachedCountry != null) {
            logger.info("Retrieved country from cache by id: {}", id);
            return cachedCountry;
        }

        Optional<Country> country = countryRepository.findById(id);
        String result;
        result = objectMapper.writeValueAsString(country.map(countryEntity -> {
            CountryDTO countryDTO = countryEntity.toDTO();
            String countryJson = null;
            try {
                countryJson = objectMapper.writeValueAsString(countryDTO);
            } catch (JsonProcessingException e) {
                logger.error("Error processing country to JSON: {}", e.getMessage());
            }
            if (countryJson != null) {
                redisCache.put("country_" + id, countryJson, 1, TimeUnit.MINUTES);
                logger.info("Retrieved country by id: {} and saved in cache", id);
            }
            return countryDTO;
        }).orElse(null));

        return result;
    }

    public String saveCountry(CountryDTO countryDTO) throws JsonProcessingException {
        Optional<Long> existingIdOptional = countryRepository.getIdByName(countryDTO.getName());
        existingIdOptional.ifPresent(countryDTO::setId);

        countryDTO.setMovies(entityIdUpdater.updateMoviesIds(countryDTO.getMovies()));
        Country savedCountry = countryRepository.save(Country.fromDTO(countryDTO));

        String result = objectMapper.writeValueAsString(savedCountry.toDTO());
        if (result != null) {
            redisCache.put("country_" + savedCountry.getId(), result, 1, TimeUnit.MINUTES);
            logger.info("Saved country in cache: {}", savedCountry);
        }
        return result;
    }

    public boolean deleteCountryById(Long id) {
        countryRepository.deleteById(id);
        redisCache.delete("country_" + id);
        logger.info("Deleted country by id: {}", id);
        return true;
    }
}
