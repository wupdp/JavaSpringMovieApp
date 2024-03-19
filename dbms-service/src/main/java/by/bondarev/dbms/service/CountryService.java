package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Country;
import by.bondarev.dbms.repository.CountryRepository;
import by.bondarev.dbms.service.cache.CountryCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import by.bondarev.dbms.dto.CountryDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final EntityIdUpdater entityIdUpdater;
    private final ObjectMapper objectMapper;
    private final CountryCache countryCache;

    @Autowired
    public CountryService(CountryRepository countryRepository, EntityIdUpdater entityIdUpdater, ObjectMapper objectMapper, CountryCache countryCache) {
        this.countryRepository = countryRepository;
        this.entityIdUpdater = entityIdUpdater;
        this.objectMapper = objectMapper;
        this.countryCache = countryCache;
    }

    public String getAllCountries() throws JsonProcessingException {
        List<Country> countries = countryRepository.findAll();
        List<CountryDTO> countryDTOs = countries.stream().map(Country::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(countryDTOs);
    }

    public String getCountryById(Long id) throws JsonProcessingException {
        CountryDTO cachedCountry = countryCache.get(id);
        if (cachedCountry != null) {
            return objectMapper.writeValueAsString(cachedCountry);
        }

        Optional<Country> country = countryRepository.findById(id);
        return objectMapper.writeValueAsString(country.map(countryEntity -> {
            CountryDTO countryDTO = countryEntity.toDTO();
            countryCache.put(id, countryDTO);
            return countryDTO;
        }).orElse(null));
    }

    public String saveCountry(CountryDTO countryDTO) throws JsonProcessingException {
        Optional<Long> existingIdOptional = countryRepository.getIdByName(countryDTO.getName());
        existingIdOptional.ifPresent(countryDTO::setId);

        countryDTO.setMovies(entityIdUpdater.updateMoviesIds(countryDTO.getMovies()));
        Country savedCountry = countryRepository.save(Country.fromDTO(countryDTO));

        countryCache.put(savedCountry.getId(), savedCountry.toDTO());

        return objectMapper.writeValueAsString(savedCountry.toDTO());
    }

    public boolean deleteCountryById(Long id) {
        countryRepository.deleteById(id);
        countryCache.evict(id);
        return true;
    }
}
