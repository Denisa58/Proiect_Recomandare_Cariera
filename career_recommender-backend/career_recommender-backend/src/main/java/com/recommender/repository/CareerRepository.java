package com.recommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.recommender.model.Career;

/**
 * Repository-ul pentru clasa career
 */
@Repository

public interface CareerRepository extends JpaRepository<Career, Long> {
}