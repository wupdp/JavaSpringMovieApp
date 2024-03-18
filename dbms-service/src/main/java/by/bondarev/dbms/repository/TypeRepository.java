package by.bondarev.dbms.repository;

import by.bondarev.dbms.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    @Query("SELECT t.id FROM Type t WHERE t.name = :name")
    Optional<Long> getIdByName(@Param("name") String name);
}
