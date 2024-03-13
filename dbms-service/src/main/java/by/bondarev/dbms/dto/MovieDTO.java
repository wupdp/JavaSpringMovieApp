package by.bondarev.dbms.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class MovieDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private int typeNumber;
    private String status;
    private Set<PersonDTO> persons;
    private Set<GenreDTO> genres;
    private Set<CountryDTO> countries;

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static MovieDTO fromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, MovieDTO.class);
    }
}