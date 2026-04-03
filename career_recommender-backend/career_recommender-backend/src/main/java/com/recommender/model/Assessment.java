
package com.recommender.model;

import jakarta.persistence.*;

/**
 * Clasa care mapeaza rezultatele autoevaluarii utilizatorului
 * Aceasta clasa transforma raspunsurile din chestionar in date stocate realizand legatura dintre
 * utilizator si abilitatile din sistem
 */

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "masterskill_id", nullable = false)
    private MasterSkill masterSkill;


    @Column(nullable = false)
    private Integer level;

    public Assessment() {
    }

    public Assessment(Long userId, MasterSkill masterSkill, Integer level) {
        this.userId = userId;
        this.masterSkill = masterSkill;
        this.level = level;
    }


    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public MasterSkill getMasterSkill() { return masterSkill; }
    public Integer getLevel() { return level; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setMasterSkill(MasterSkill masterSkill) { this.masterSkill = masterSkill; }
    public void setLevel(Integer level) { this.level = level; }
}