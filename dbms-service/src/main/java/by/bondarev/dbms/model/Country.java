package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.CountryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "countries", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            })
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