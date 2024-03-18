package by.bondarev.moviesearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieApiResponse {
    private List<MovieInfo> docs;
    private int total;
    private int limit;
    private int page;
    private int pages;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class MovieInfo {
    private Long id;
    private String name;
    private String description;
    private String type;
    @JsonProperty("typeNumber")
    private int typeId;
    private String status;
    @JsonProperty("statusId")
    private int statusCode;
    private List<GenreDTO> genres;
    private List<CountryDTO> countries;
}
