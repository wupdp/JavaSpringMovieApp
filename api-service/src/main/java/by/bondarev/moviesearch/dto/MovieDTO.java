package by.bondarev.moviesearch.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class MovieDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private int typeNumber;
    private String status;
    private int statusId;
    private Set<PersonDTO> persons;
    private Set<GenreDTO> genres;
    private Set<CountryDTO> countries;

    public static MovieDTO fromApiResponse(MovieApiResponse response) {
        MovieInfo movieInfo = response.getDocs().get(0);
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setId(0L);
        movieDTO.setName(movieInfo.getName());
        movieDTO.setDescription(movieInfo.getDescription());
        movieDTO.setType(movieInfo.getType());
        movieDTO.setTypeNumber(movieInfo.getTypeId());
        movieDTO.setStatus(movieInfo.getStatus());
        movieDTO.setStatusId(0);
        movieDTO.setGenres(new HashSet<>(movieInfo.getGenres()));
        movieDTO.setCountries(new HashSet<>(movieInfo.getCountries()));
        return movieDTO;
    }
    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static MovieDTO fromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, MovieDTO.class);
    }
}