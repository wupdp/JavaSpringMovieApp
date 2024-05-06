package by.bondarev.dbms.repository;

import by.bondarev.dbms.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT c.id FROM Movie c WHERE c.name = :name")
    Optional<Long> getIdByName(@Param("name") String name);
    List<Movie> findByPersonsName(String personName);
    List<Movie> findByName(String name);
    List<Movie> findByGenresName(String genreName);
    List<Movie> findByCountriesName(String countryName);
    boolean existsByName(String name);
}