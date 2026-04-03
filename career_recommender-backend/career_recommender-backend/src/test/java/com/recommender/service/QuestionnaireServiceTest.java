package com.recommender.service;
import com.recommender.dto.AssessmentDto;
import com.recommender.dto.QuestionnaireSubmissionDto;
import com.recommender.model.Assessment;
import com.recommender.model.MasterSkill;
import com.recommender.repository.AssessmentRepository;
import com.recommender.repository.MasterSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //activeaza suportul Mockito pentru a putea folosi adnotarile @Mock si @InjectMocks
public class QuestionnaireServiceTest {

    //il folosim pentru a crea odublura a obiectului userRepository fara sa ne conectam la baza de date, doar simulam comportamentul ei
    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private MasterSkillRepository masterSkillRepository;

    //creeaza o instanta reala a clasei UserService la care ii da dublura creata mai sus
    @InjectMocks
    private QuestionnaireService questionnaireService;

    private Long userId;
    private MasterSkill skillJava;

    //aceasta metoda se executa automata inainte de fiecare test pentru a reseta datele
    @BeforeEach
    void setUp()
    {
        userId = 1L;
        skillJava = new MasterSkill("Java", "tehnic");
        skillJava.setId(10L);

    }

    @Test
    void salvare_raspunsuri()
    {
        //simulam ce primim de la frontend adica skill id 20 si nivel 4
        AssessmentDto assessmentDto = new AssessmentDto();
        assessmentDto.setSkillId(10L);
        assessmentDto.setLevel(4);

        QuestionnaireSubmissionDto submissionDto = new QuestionnaireSubmissionDto();
        submissionDto.setAssessments(List.of(assessmentDto));

        //cand se cauta skill-ul cu acel id se returneaza skillJava creat mai sus
        when(masterSkillRepository.findById(10L)).thenReturn(Optional.of(skillJava));

        //apelam metoda
        questionnaireService.saveAssessments(userId, submissionDto);

        //verificam daca a apelat stergerea evaluarilor vechi pentru aces user
        Mockito.verify(assessmentRepository).deleteByUserId(userId);

        //verificam daca a cautat in baza de date skill-ul primit
        Mockito.verify(masterSkillRepository).findById(10L);

        //verificam daca a salvat evaluarea in baza de date
        Mockito.verify(assessmentRepository).save(any(Assessment.class));

    }




}
