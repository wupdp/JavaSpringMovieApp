package by.bondarev.dbms.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Set;

@Data
public class PersonDTO {
    private Long id;
    private String name;
    private String description;
    private Set<MovieDTO> movies;

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static PersonDTO fromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, PersonDTO.class);
    }
}
