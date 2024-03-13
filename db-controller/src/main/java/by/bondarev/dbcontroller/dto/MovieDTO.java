package by.bondarev.dbcontroller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MovieDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private int typeNumber;
    private String status;
    private Set<Long> personIds;
    private Set<Long> genreIds;
    private Set<Long> countryIds;
}

