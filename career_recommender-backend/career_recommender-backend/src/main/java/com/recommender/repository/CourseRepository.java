package com.recommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.recommender.model.Course;
import java.util.List;
@Repository
/**
 * Interfata responsabila pentru interactiunea cu tabelul de cursuri din baza de date
 * Pe langa operatiile de salvare si cautare aceasta permmite verificarea existentei cursurilor dupa titlu
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTitleContainingIgnoreCase(String title);
    boolean existsByTitle(String title);
}