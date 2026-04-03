package com.recommender.model;

import jakarta.persistence.*;

/**
 * Clasa folosita pentru a oferi recomandari de cursuri  externe
 * Aceasta stocheaza informatiile disponibile despre cursurile de pe platforma Exercism in baza de date
 * Ofera detalii despre titlu, continut si link-ul direct catre curs
 */

@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", length=255, nullable=false)
    private String title;

    @Column(name="description", columnDefinition="TEXT", nullable = true)
    private String description;

    @Column(name="url", length=512, nullable=true)
    private String url;

    @Column(name="platform", length=50)
    private String platform;

    private double matchScore;

    public Course(){
    }

    public Course(String title, String description, String url, String platform){
        this.title = title;
        this.description = description;
        this.url = url;
        this.platform = platform;
    }


    public Long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getUrl(){
        return url;
    }
    public String getPlatform(){
        return platform;
    }
    public double getMatchScore(){return matchScore;}


    public void setId(Long id){
        this.id = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setPlatform(String platform){
        this.platform = platform;
    }
    public void setMatchScore(double matchScore){ this.matchScore = matchScore;}
}