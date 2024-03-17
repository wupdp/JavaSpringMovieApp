package by.bondarev.dbms.repository;

import by.bondarev.dbms.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    @Query("SELECT s.id FROM Status s WHERE s.name = :name")
    Optional<Long> getIdByName(@Param("name") String name);
}
