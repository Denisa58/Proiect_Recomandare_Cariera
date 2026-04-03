package com.recommender.model;

import jakarta.persistence.*;

/**
 * Clasa care defineste modelul unei cariere in baza de date
 * Pastreaza toate informatiile despre cariere pe care aplicatia le poate recomanda
 */


@Entity
@Table(name="careers")
public class Career {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;


    @Column(name="name", unique=true, length=100, nullable=false)
    private String name;

    @Column(name="description", columnDefinition="TEXT") // Coloană de text lung
    private String description;

    @Column(name="required_experience", length=50)
    private String requiredExperience;

    @Column(name="median_salary")
    private Integer medianSalary;

    public Career(){
    }

    public Career(String name, String description, String requiredExperience, Integer medianSalary){
        this.name = name;
        this.description = description;
        this.requiredExperience = requiredExperience;
        this.medianSalary = medianSalary;
    }

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getRequiredExperience(){
        return requiredExperience;
    }
    public Integer getMedianSalary(){
        return medianSalary;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setRequiredExperience(String requiredExperience){
        this.requiredExperience = requiredExperience;
    }
    public void setMedianSalary(Integer medianSalary){
        this.medianSalary = medianSalary;
    }
}