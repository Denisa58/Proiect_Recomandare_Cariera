package com.recommender.controller;

import com.recommender.dto.RecommendationDto;
import com.recommender.dto.QuestionnaireSubmissionDto;
import com.recommender.model.MasterSkill;
import com.recommender.repository.MasterSkillRepository;
import com.recommender.service.RecommendationService;
import com.recommender.service.QuestionnaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clasa care gestioneaza rezultatele chestionarului si face recomandari de cariera
 * Livreaza lista de abilitati pentru interfata, primeste si salveaza raspunsurile utilizatorului
 * declanseaza algoritmul de calcul pentru a returna cele mai potrivite cariere
  */

@RestController
@RequestMapping("/api/recommendation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecommendationController {

    private final MasterSkillRepository masterSkillRepository;

    private final RecommendationService recommendationService;

    private final QuestionnaireService questionnaireService;

    public RecommendationController(
            MasterSkillRepository masterSkillRepository,
            RecommendationService recommendationService,
            QuestionnaireService questionnaireService) {

        this.masterSkillRepository = masterSkillRepository;
        this.recommendationService = recommendationService;
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("/skills")
    public ResponseEntity<List<MasterSkill>> getAllSkills() {

        List<MasterSkill> skills = masterSkillRepository.findAll();
        return ResponseEntity.ok(skills);
    }

    @PostMapping("/submit/{userId}")
    public ResponseEntity<String> submitQuestionnaire(

            @PathVariable Long userId,
            @RequestBody QuestionnaireSubmissionDto submissionDto) {

        try {

            questionnaireService.saveAssessments(userId, submissionDto);
            return ResponseEntity.ok("Evaluarea a fost trimisă și salvată cu succes!");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la salvare: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare internă la salvarea datelor.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(@PathVariable Long userId) {

        List<RecommendationDto> recommendations = recommendationService.getCareerRecommendations(userId);

        if (recommendations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(recommendations);
    }
}