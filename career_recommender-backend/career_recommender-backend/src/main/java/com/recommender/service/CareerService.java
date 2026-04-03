package com.recommender.service;

import com.recommender.model.Career;
import com.recommender.repository.CareerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Componenta de tip Service care gestioneaza logica de business pentru cariere.
 * Face legatura intre Repository si Controllerasigurand accesul la profesiile disponibile  in baza de date.
 */
@Service
public class CareerService {


    private final CareerRepository careerRepository;


    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }


    public List<Career> getAllCareers() {
        return careerRepository.findAll();
    }


}