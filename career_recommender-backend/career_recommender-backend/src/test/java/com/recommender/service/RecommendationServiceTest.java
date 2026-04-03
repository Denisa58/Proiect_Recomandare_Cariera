package com.recommender.service;

import com.recommender.dto.RecommendationDto;
import com.recommender.model.Assessment;
import com.recommender.model.Career;
import com.recommender.model.CareerSkillRequirement;
import com.recommender.model.MasterSkill;
import com.recommender.repository.AssessmentRepository;
import com.recommender.repository.CareerSkillRequirementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //activeaza suportul Mockito pentru a putea folosi adnotarile @Mock si @InjectMocks
public class RecommendationServiceTest {

    //il folosim pentru a crea odublura a obiectului userRepository fara sa ne conectam la baza de date, doar simulam comportamentul ei
    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private CareerSkillRequirementRepository requirementRepository;

    //creeaza o instanta reala a clasei UserService la care ii da dublura creata mai sus
    @InjectMocks
    private RecommendationService recommendationService;

    private Career itCareer;
    private MasterSkill javaSkill;

    //aceasta metoda se executa automata inainte de fiecare test pentru a reseta datele
    @BeforeEach
    void setUp()
    {
        itCareer = new Career("IT Specialist", "descriere", "avansat", 5000);
        javaSkill = new MasterSkill("Java", "Tehnic");
    }

    @Test
    void getCareerRecommendation()
    {
        //initializam datele de care avem nevoie
        Long userId = 1L;

        //utilizatorul
        Assessment userAssessment = new Assessment();
        userAssessment.setMasterSkill(javaSkill);
        userAssessment.setLevel(5);

        //cariera
        CareerSkillRequirement requirement = new CareerSkillRequirement();
        requirement.setCareer(itCareer);
        requirement.setMasterSkill(javaSkill);
        requirement.setRequiredLevel(5);

        //verificam daca se gaseste utilizatorul si returnam datele de mai sus sub forma de lista
        when(assessmentRepository.findByUserId(userId)).thenReturn(List.of(userAssessment));
        when(requirementRepository.findAll()).thenReturn(List.of(requirement));

        //apelam metoda din RecommendationService
        List<RecommendationDto> results = recommendationService.getCareerRecommendations(userId);

        //verificam daca scorul se potriveste
        assertEquals(results.isEmpty(), false);
        assertEquals(1.0,results.get(0).getMatchScore());
        assertEquals("IT Specialist",results.get(0).getCareerName());

    }

    @Test
    void getCareerRecommendation_partial()
    {
        //initializam datele de care avem nevoie
        Long userId = 1L;

        //utilizatorul
        Assessment userAssessment = new Assessment();
        userAssessment.setMasterSkill(javaSkill);
        userAssessment.setLevel(2);

        //cariera
        CareerSkillRequirement requirement = new CareerSkillRequirement();
        requirement.setCareer(itCareer);
        requirement.setMasterSkill(javaSkill);
        requirement.setRequiredLevel(4);

        //verificam daca se gaseste utilizatorul si returnam datele de mai sus sub forma de lista
        when(assessmentRepository.findByUserId(userId)).thenReturn(List.of(userAssessment));
        when(requirementRepository.findAll()).thenReturn(List.of(requirement));

        //apelam metoda din RecommendationService
        List<RecommendationDto> results = recommendationService.getCareerRecommendations(userId);

        //verificam formula de calcul partial
        assertEquals(0.5,results.get(0).getMatchScore());

    }

    @Test
    void getCareerRecommendation_sortare()
    {
        //initializam datele de care avem nevoie
        Long userId = 1L;

        //skill ul
        MasterSkill javaSkill = new MasterSkill("Java", "tehnic");

        //utilizatorul
        Assessment userAssessment = new Assessment();
        userAssessment.setMasterSkill(javaSkill);
        userAssessment.setLevel(3);

        //cream prima cariera
        Career dev = new Career("Dev", "descriere","mediu",7000);
        CareerSkillRequirement reqA = new CareerSkillRequirement();
        reqA.setCareer(dev);
        reqA.setMasterSkill(javaSkill);
        reqA.setRequiredLevel(3);

        //cream a doua cariera
        Career data = new Career("Data Science", "descriere", "avansat",6500);
        CareerSkillRequirement reqB = new CareerSkillRequirement();
        reqB.setCareer(data);
        reqB.setMasterSkill(javaSkill);
        reqB.setRequiredLevel(5);

        //returnam o lista cu datele despre user
        when(assessmentRepository.findByUserId(userId)).thenReturn(List.of(userAssessment));

        //returneaza ambele reguli pe care algoritmul trebuie sa le compare
        when(requirementRepository.findAll()).thenReturn(List.of(reqA,reqB));

        //apelam metoda din RecommendationService
        List<RecommendationDto> results = recommendationService.getCareerRecommendations(userId);

        //testam sortarea
        assertEquals("Dev", results.get(0).getCareerName());
        assertEquals(1.0,results.get(0).getMatchScore());

        assertEquals("Data Science", results.get(1).getCareerName());
        assertEquals(0.6,results.get(1).getMatchScore());

        assertTrue(results.get(0).getMatchScore() > results.get(1).getMatchScore());



    }
    @Test
    void getCareerRecommendation_verificareRotunjire() {
        Long userId = 1L;

        // Setăm ID-uri diferite
        MasterSkill s1 = new MasterSkill("Java", "Technical");
        s1.setId(1L);
        MasterSkill s2 = new MasterSkill("SQL", "Technical");
        s2.setId(2L);
        MasterSkill s3 = new MasterSkill("Python", "Technical");
        s3.setId(3L);

        // Userul are doar primele 2 skill-uri
        Assessment a1 = new Assessment(userId, s1, 5);
        Assessment a2 = new Assessment(userId, s2, 5);

        Career c = new Career("FullStack", "desc", "Mediu", 8000);
        CareerSkillRequirement r1 = new CareerSkillRequirement(c, s1, 5);
        CareerSkillRequirement r2 = new CareerSkillRequirement(c, s2, 5);
        CareerSkillRequirement r3 = new CareerSkillRequirement(c, s3, 5);

        when(assessmentRepository.findByUserId(userId)).thenReturn(List.of(a1, a2));
        when(requirementRepository.findAll()).thenReturn(List.of(r1, r2, r3));

        List<RecommendationDto> results = recommendationService.getCareerRecommendations(userId);

        assertFalse(results.isEmpty());

        assertEquals(0.67, results.get(0).getMatchScore());
    }
}
