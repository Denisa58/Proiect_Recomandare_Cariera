
package com.recommender.model;

import jakarta.persistence.*;

/**
 * Clasa care stabileste cerintele pentru fiecare cariera
 * Face legatura intre tabelul de cariere si cel de skill-uri specificand exact ce abilitati sunt necesare pentru o anumita cariera
 * si care este nivelul minim pe care un utilizator trebuie sa il atinga pentru a fi compatibil
 */

@Entity
@Table(name = "career_skill_requirements")
public class CareerSkillRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;


    @ManyToOne
    @JoinColumn(name = "masterskill_id", nullable = false)
    private MasterSkill masterSkill;

    @Column(nullable = false)
    private Integer requiredLevel;

    public CareerSkillRequirement() {}

    public CareerSkillRequirement(Career career, MasterSkill masterSkill, Integer requiredLevel) {
        this.career = career;
        this.masterSkill = masterSkill;
        this.requiredLevel = requiredLevel;
    }


    public Long getId() { return id; }
    public Career getCareer() { return career; }
    public MasterSkill getMasterSkill() { return masterSkill; }
    public Integer getRequiredLevel() { return requiredLevel; }


    public void setId(Long id) { this.id = id; }
    public void setCareer(Career career) { this.career = career; }
    public void setMasterSkill(MasterSkill masterSkill) { this.masterSkill = masterSkill; }
    public void setRequiredLevel(Integer requiredLevel) { this.requiredLevel = requiredLevel; }
}