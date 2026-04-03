package com.recommender.service;

import com.recommender.model.MasterSkill;
import com.recommender.repository.MasterSkillRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Componenta Service responsabila cu gestionarea competentelor(skill-urilor)
 * Ofera acces la skill-urile predefinite din baza de date atat pentru evaluarile utilizatorului
 * cat si pentru cerintele specifice fiecarei cariere
 */

@Service
public class MasterSkillService {

    private final MasterSkillRepository masterSkillRepository;

    public MasterSkillService(MasterSkillRepository masterSkillRepository) {
        this.masterSkillRepository = masterSkillRepository;
    }

    public List<MasterSkill> getAllMasterSkills() {
        return masterSkillRepository.findAll();
    }
}