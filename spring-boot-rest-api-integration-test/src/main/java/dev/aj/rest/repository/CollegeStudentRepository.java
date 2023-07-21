package dev.aj.rest.repository;

import dev.aj.rest.model.CollegeStudent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeStudentRepository extends JpaRepository<CollegeStudent, Long> {

    Optional<CollegeStudent> findByEmail(String email);

    @Override
    List<CollegeStudent> findAll();

}
