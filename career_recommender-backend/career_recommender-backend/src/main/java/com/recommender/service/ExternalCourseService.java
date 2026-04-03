package com.recommender.service;

import com.recommender.model.Course;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Componenta Service care gestioneaza comunicarea cu platforma Exercism,preluand cursurile
 * acestea vin in format JSOn deci trebuie facuta o mapare a cursurilor externe catre modelul intern
 */
@Service
public class ExternalCourseService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String EXERCISM_API_URL = "https://exercism.org/api/v2/tracks";

    public List<Course> fetchExternalCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(EXERCISM_API_URL, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode tracksNode = root.path("tracks");

            if (tracksNode.isArray()) {
                for (JsonNode node : tracksNode) {
                    Course course = new Course();

                    course.setTitle(node.path("title").asText());

                    String desc = node.path("description").asText();
                    course.setDescription(desc != null && !desc.isEmpty() ? desc : "No description available");

                    course.setUrl(node.path("web_url").asText());

                    course.setPlatform("Exercism");

                    courses.add(course);
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la preluarea datelor de la Exercism: " + e.getMessage());
        }
        return courses;
    }
}