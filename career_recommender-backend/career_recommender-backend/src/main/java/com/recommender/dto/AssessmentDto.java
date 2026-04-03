
package com.recommender.dto;

/**
 * Aceasta clasa transforma raspunsul utilizatorului pentru un anumit skill astfel incat sa fie usor de transportat de la frontend a backend
 * Transporta doar informatiile necesare de la formularul completate de utilizator in browser
 */
public class AssessmentDto {


    private Long skillId;

    private Integer level;

    public AssessmentDto() {}

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

}