package by.bondarev.dbms.model;

import by.bondarev.dbms.dto.GenreDTO;
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
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            })
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