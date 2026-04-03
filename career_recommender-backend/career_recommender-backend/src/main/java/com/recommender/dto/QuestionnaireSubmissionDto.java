
package com.recommender.dto;

import java.util.List;

/**
 * Clasa care aduna raspunsurile fiecarui utilizator in urma completarii chestionarului
 * Grupeaza raspunsurile individuala intr-o singura lista permitand aplicatiei sa primeasca si sa proceseze toate competentele evaluate
 */
public class QuestionnaireSubmissionDto {


    private List<AssessmentDto> assessments;

    public QuestionnaireSubmissionDto() {}

    public List<AssessmentDto> getAssessments() { return assessments; }

    public void setAssessments(List<AssessmentDto> assessments) { this.assessments = assessments; }

}