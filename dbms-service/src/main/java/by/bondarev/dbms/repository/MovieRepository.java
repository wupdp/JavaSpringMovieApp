package by.bondarev.dbms.repository;

import by.bondarev.dbms.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByPersonsName(String personName);

    @Query("SELECT DISTINCT m FROM Movie m JOIN FETCH m.genres g JOIN FETCH m.persons p JOIN FETCH m.countries c")
    List<Movie> findAllFetchGenresAndPersonsAndCountries();
    List<Movie> findByName(String name);
    List<Movie> findByGenresName(String genreName);
    List<Movie> findByCountriesName(String countryName);
}