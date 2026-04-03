package com.recommender.service;

import com.recommender.dto.AssessmentDto;
import com.recommender.dto.QuestionnaireSubmissionDto;
import com.recommender.model.Assessment;
import com.recommender.model.MasterSkill;
import com.recommender.repository.AssessmentRepository;
import com.recommender.repository.MasterSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Componenta Service responsabila pentru procesarea rezultatelor din chestionarul de autoevaluare
 * Clasa gestioneaza competentele utilizatorului, asigurand actualizarea profilului daca acesta decide sa refaca chestionarul
 */
@Service
public class QuestionnaireService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private MasterSkillRepository masterSkillRepository;

    @Transactional
    public void saveAssessments(Long userId, QuestionnaireSubmissionDto submissionDto) {

        assessmentRepository.deleteByUserId(userId);

        if (submissionDto.getAssessments() == null || submissionDto.getAssessments().isEmpty()) {
            System.out.println("AVERTISMENT: Lista de evaluări primită este GOALĂ!");
            return;
        }

        for (AssessmentDto dto : submissionDto.getAssessments()) {
            System.out.println("DEBUG: Procesare Skill ID: " + dto.getSkillId() + " cu Level: " + dto.getLevel());

            MasterSkill masterSkill = masterSkillRepository.findById(dto.getSkillId())
                    .orElseThrow(() -> new RuntimeException("MasterSkill cu ID " + dto.getSkillId() + " nu a fost găsit."));

            Assessment assessment = new Assessment();
            assessment.setUserId(userId);
            assessment.setMasterSkill(masterSkill);
            assessment.setLevel(dto.getLevel());

            assessmentRepository.save(assessment);
        }

        System.out.println("DEBUG: Salvare finalizată cu succes în baza de date.");
    }
}