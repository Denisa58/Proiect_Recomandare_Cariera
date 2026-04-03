package com.recommender.model;

import jakarta.persistence.*;

/**
 * Clasa care contine skill-urile disponibile
 * Defineste lista oficiala de competente pe care utilizatorul le poate avea
 * si pe care o cariera le poate solicita
 */
@Entity
@Table(name="master_skills")
public class MasterSkill {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //in baza de date id ul se incrementeaza automat
    private Long id;

    @Column(name="skill_name", length=100, nullable=false)
    private String skillName;

    @Column(name="skill_type", length=50)
    private String skillType;

    public MasterSkill() {
    }

    public MasterSkill(String skillName, String skillType) {
        this.skillName = skillName;
        this.skillType = skillType;
    }

    public Long getId() {
        return id;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillType() {
        return skillType;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }
}