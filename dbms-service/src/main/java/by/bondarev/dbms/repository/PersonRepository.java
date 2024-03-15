package by.bondarev.dbms.repository;

import by.bondarev.dbms.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT c.id FROM Person c WHERE c.name = :name")
    Optional<Long> getIdByName(@Param("name") String name);
}
