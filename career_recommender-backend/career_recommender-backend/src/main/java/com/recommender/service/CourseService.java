package com.recommender.service;

import com.recommender.model.Course;
import com.recommender.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Componenta Service care gestioneaza cursurile din baza de date
 * Face legatura intre Repository si restul aplicatiei oferind o interfata simplificata pentru preluarea cursurilor
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


}