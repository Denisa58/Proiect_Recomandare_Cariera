
package com.recommender.repository;

import com.recommender.model.MasterSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interfata pentru gestiunea abilitatilor din baza de date
 * Permite identitficarea competentelor dupa denumire
 */
public interface MasterSkillRepository extends JpaRepository<MasterSkill, Long> {

    Optional<MasterSkill> findBySkillName(String skillName);
}