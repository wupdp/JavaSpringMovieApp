package by.bondarev.dbms.service;

import by.bondarev.dbms.model.Country;
import by.bondarev.dbms.repository.CountryRepository;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public CountryService(CountryRepository countryRepository, ObjectMapper objectMapper) {
        this.countryRepository = countryRepository;
        this.objectMapper = objectMapper;
    }

    public String getAllCountries() throws JsonProcessingException {
        List<Country> countries = countryRepository.findAll();
        List<CountryDTO> countryDTOs = countries.stream().map(Country::toDTO).collect(Collectors.toList());
        return objectMapper.writeValueAsString(countryDTOs);
    }

    public String getCountryById(Long id) throws JsonProcessingException {
        Optional<Country> country = countryRepository.findById(id);
        return objectMapper.writeValueAsString(country.map(Country::toDTO).orElse(null));
    }

    public String saveCountry(Country country) throws JsonProcessingException {
        Country savedCountry = countryRepository.save(country);
        return objectMapper.writeValueAsString(savedCountry.toDTO());
    }

    public boolean deleteCountryById(Long id) {
        countryRepository.deleteById(id);
        return true;
    }
}
