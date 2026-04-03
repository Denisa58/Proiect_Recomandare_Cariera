package com.recommender.dto;

/**
 * Clasa utilizata pentru a trimite rezultatele finale catre utilizator
 * Contine numele carierei recomandate, descrierea acesteia si cat de bine se potriveste cu profilul utilizatorului
 * Transforma scorurile matematice in procente
 */

public class RecommendationDto {

    private String careerName;

    private String careerDescription;

    private double matchScore;

    private String matchPercentage;

    public RecommendationDto(String careerName, String careerDescription, double matchScore) {
        this.careerName = careerName;
        this.careerDescription = careerDescription;
        this.matchScore = matchScore;
        this.matchPercentage = String.format("%.2f%%", matchScore * 100);
    }


    public RecommendationDto() {
    }



    public String getCareerName() {
        return careerName;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public String getMatchPercentage() {
        return matchPercentage;
    }


    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public void setCareerDescription(String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
        this.matchPercentage = String.format("%.2f%%", matchScore * 100);
    }


    public void setMatchPercentage(String matchPercentage) {
        this.matchPercentage = matchPercentage;
    }
}