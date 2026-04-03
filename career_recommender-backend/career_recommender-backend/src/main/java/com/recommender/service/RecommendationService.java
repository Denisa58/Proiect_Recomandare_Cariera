package com.recommender.service;

import com.recommender.dto.RecommendationDto;
import com.recommender.model.Assessment;
import com.recommender.model.CareerSkillRequirement;
import com.recommender.repository.AssessmentRepository;
import com.recommender.repository.CareerSkillRequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clasa care implementeaza algoritmul de recomandare de cariera
 * Aceasta compara nivelul abilitatilor pe care le-a completat utilizatorul in chestionar cu nivelul cerut de fiecare cariera
 * La final ofera o lista de cariere potrivite, sortate de la cea mai potrivita pana la cea mai putin potrivita
 * calculand un procent de potrivire pentru fiecare
 */

@Service
public class RecommendationService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CareerSkillRequirementRepository requirementRepository;

    @Transactional
    public List<RecommendationDto> getCareerRecommendations(Long userId) {

        List<Assessment> userAssessments = assessmentRepository.findByUserId(userId);

        Map<Long, Integer> userSkillMap = userAssessments.stream()
                .collect(Collectors.toMap(
                        a -> a.getMasterSkill().getId(),
                        Assessment::getLevel,
                        (existing, replacement) -> existing // Păstrează prima valoare în caz de duplicat
                ));

        List<CareerSkillRequirement> allRequirements = requirementRepository.findAll();

        Map<String, RecommendationScore> careerScores = new HashMap<>();

        for (CareerSkillRequirement req : allRequirements) {

            String careerName = req.getCareer().getName();
            Long skillId = req.getMasterSkill().getId();
            int requiredLevel = req.getRequiredLevel();

            int userLevel = userSkillMap.getOrDefault(skillId, 0);

            careerScores.putIfAbsent(careerName, new RecommendationScore());
            RecommendationScore score = careerScores.get(careerName);

            score.totalRequiredSkills++;

            if (userLevel > 1) {
                if (userLevel >= requiredLevel) {
                    score.matchedSkills += 1.0;
                } else {
                    score.matchedSkills += (double) userLevel / requiredLevel;
                }
            }
        }

        List<RecommendationDto> recommendations = new ArrayList<>();

        for (Map.Entry<String, RecommendationScore> entry : careerScores.entrySet()) {
            RecommendationScore score = entry.getValue();
            String careerName = entry.getKey();

            if (score.totalRequiredSkills > 0) {

               double rawPercentage = (double) score.matchedSkills / score.totalRequiredSkills;

               double matchPercentage = Math.round(rawPercentage * 100.0) / 100.0;


                String careerDescription = allRequirements.stream()
                        .filter(req -> req.getCareer().getName().equals(careerName))
                        .findFirst()
                        .map(req -> req.getCareer().getDescription())
                        .orElse("Descriere indisponibilă");


                recommendations.add(new RecommendationDto(careerName, careerDescription, matchPercentage));
            }
        }

        recommendations.sort(Comparator.comparing(RecommendationDto::getMatchScore).reversed());

        return recommendations;
    }

    // Clasa internă pentru a stoca rezultatele partiale
    private static class RecommendationScore {
        double matchedSkills = 0.0;
        int totalRequiredSkills = 0;
    }
}