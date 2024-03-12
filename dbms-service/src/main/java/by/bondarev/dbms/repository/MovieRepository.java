package by.bondarev.dbms.repository;

import by.bondarev.dbms.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m INNER JOIN m.persons p WHERE p.name = ?1")
    List<Movie> findByPersonName(String personName);
    List<Movie> findByName(String name);
    List<Movie> findByGenresName(String genreName);

    List<Movie> findByCountriesName(String countryName);
}