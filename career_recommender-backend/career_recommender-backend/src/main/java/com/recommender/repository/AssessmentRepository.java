package com.recommender.repository;

import com.recommender.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfata care gestioneaza accesul la date pentru evaluarile utilizatorului
 * Face legatura directa cu baza de date permitand cautarea notelor oferite de utilizator sau stergerea acestora cand chestionarul este reluat
 * Foloseste Spring Data JPA pentru a genera automat interogarile SQL necesare
 */
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
  List<Assessment> findByUserId(Long userId);

  void deleteByUserId(Long userId);
}