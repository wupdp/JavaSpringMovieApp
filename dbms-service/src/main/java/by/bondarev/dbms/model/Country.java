package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.CountryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "countries")
    private Set<Movie> movies = new HashSet<>();

    public CountryDTO toDTO() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(this.id);
        countryDTO.setName(this.name);
        return countryDTO;
    }

    public static Country fromDTO(CountryDTO countryDTO) {
        Country country = new Country();
        country.setId(countryDTO.getId());
        country.setName(countryDTO.getName());
        return country;
    }
}