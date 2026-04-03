package com.recommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.recommender.model.User;
import java.util.Optional;

/**
 * Interfata responsabila pentru gestiunea datelor de acces ale utilizatorului
 * Asigura cautarea unui cont de utilizator dupa numele utilizatorului si permite verificarea daca un nume este deja luat
 * atunci cand utilizatorul incearca sa se inregistreze
 */
@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}