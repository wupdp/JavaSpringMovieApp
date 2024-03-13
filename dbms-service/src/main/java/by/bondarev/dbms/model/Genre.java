package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.GenreDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();

    public GenreDTO toDTO() {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(this.id);
        genreDTO.setName(this.name);
        return genreDTO;
    }

    public static Genre fromDTO(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setId(genreDTO.getId());
        genre.setName(genreDTO.getName());
        return genre;
    }

}