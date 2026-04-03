
package com.recommender.controller;

import com.recommender.dto.QuestionnaireSubmissionDto;
import com.recommender.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller care gestioneaza interactiunea finala a utilizatorului cu chestionarul
 * Primeste lista completa de abilitati si coordoneaza procesul de salvare a acestora, asigurandu-se ca
 * profilul utilizatorului este actualizat corect in sistem
 */
@RestController
@RequestMapping("/api/questionnaire")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionnaireController {

    @Autowired

    private QuestionnaireService questionnaireService;

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
}