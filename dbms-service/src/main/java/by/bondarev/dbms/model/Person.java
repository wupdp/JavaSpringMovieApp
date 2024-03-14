package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.PersonDTO;
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
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Set<Movie> movies = new HashSet<>();

    public PersonDTO toDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(this.id);
        personDTO.setName(this.name);
        personDTO.setDescription(this.description);
        return personDTO;
    }

    public static Person fromDTO(PersonDTO personDTO) {
        Person person = new Person();
        person.setId(personDTO.getId());
        person.setName(personDTO.getName());
        person.setDescription(personDTO.getDescription());
        return person;
    }
}
