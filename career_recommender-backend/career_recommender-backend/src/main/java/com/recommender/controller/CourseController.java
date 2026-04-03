package com.recommender.controller;

import com.recommender.model.Course;
import com.recommender.repository.CourseRepository;
import com.recommender.service.ExternalCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Acest Controller implementeaza sistemul de recomadare care decide in timp real daca exista un curs potrivit,
 * daca trebuie sa caute resurse teoretice externe (Wikipedia) si cum sa sincronizeze resursele educationale
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseController {

    private final CourseRepository courseRepository;
    private final ExternalCourseService externalCourseService;

    public CourseController(CourseRepository courseRepository, ExternalCourseService externalCourseService) {
        this.courseRepository = courseRepository;
        this.externalCourseService = externalCourseService;
    }

    @GetMapping("/recommend-ai")
    public ResponseEntity<List<Course>> getAIRecommendations(@RequestParam String careerName) {
        System.out.println(" Analizăm profilul pentru cariera: " + careerName);

        List<Course> allLocalCourses = courseRepository.findAll();
        List<Course> scoredRecommendations = new ArrayList<>();

        for (Course course : allLocalCourses) {
            String targetText = (course.getTitle() + " " + course.getDescription()).toLowerCase();

            double score = calculateSimilarityScore(careerName.toLowerCase(), targetText);

            if (score > 0.05) {
                course.setMatchScore(score * 100);
                course.setPlatform("Exercism (IT)");
                scoredRecommendations.add(course);
            }
        }

        scoredRecommendations.sort((c1, c2) -> Double.compare(c2.getMatchScore(), c1.getMatchScore()));

        List<Course> topResults = scoredRecommendations.stream().limit(5).collect(Collectors.toList());

        if (topResults.isEmpty()) {
            System.out.println("AI Engine: Nu am găsit cursuri tehnice. Extindem căutarea pe Wikipedia...");
            List<Course> wikiResults = fetchFromWikipedia(careerName);
            wikiResults.forEach(c -> {
                c.setMatchScore(70.0); // Scor fix pentru rezultate teoretice
                c.setPlatform("Wikipedia (Teorie)");
            });
            return ResponseEntity.ok(wikiResults);
        }

        return ResponseEntity.ok(topResults);
    }

    private double calculateSimilarityScore(String career, String courseInfo) {
        Set<String> s1 = tokenize(career);
        Set<String> s2 = tokenize(courseInfo);

        Set<String> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);

        Set<String> union = new HashSet<>(s1);
        union.addAll(s2);

        if (union.isEmpty()) return 0.0;
        return (double) intersection.size() / union.size();
    }

    private Set<String> tokenize(String text) {
        return Arrays.stream(text.split("\\W+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toSet());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Course>> smartSearch(@RequestParam(required = false, defaultValue = "") String keyword) {
        if (keyword.isEmpty()) {
            return ResponseEntity.ok(courseRepository.findAll());
        }

        String searchKey = keyword.toLowerCase().trim();
        List<Course> localCourses = courseRepository.findByTitleContainingIgnoreCase(searchKey);

        if (localCourses != null && !localCourses.isEmpty()) {
            localCourses.forEach(c -> c.setPlatform("Exercism (IT)"));
            return ResponseEntity.ok(localCourses);
        }

        List<Course> wikiResults = fetchFromWikipedia(searchKey);
        if (!wikiResults.isEmpty()) {
            wikiResults.forEach(c -> c.setPlatform("Wikipedia (Teorie)"));
            return ResponseEntity.ok(wikiResults);
        }

        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/sync")
    public ResponseEntity<String> syncWithExercism() {
        List<Course> externalCourses = externalCourseService.fetchExternalCourses();
        if (externalCourses.isEmpty()) {
            return ResponseEntity.status(503).body("Nu s-au putut prelua datele de la Exercism.");
        }

        int addedCount = 0;
        for (Course course : externalCourses) {
            if (!courseRepository.existsByTitle(course.getTitle())) {
                courseRepository.save(course);
                addedCount++;
            }
        }
        return ResponseEntity.ok("Sincronizare finalizată. Cursuri noi adăugate: " + addedCount);
    }

    private List<Course> fetchFromWikipedia(String topic) {
        String urlWiki = "https://ro.wikipedia.org/w/api.php?action=opensearch&search=" + topic + "&limit=8&format=json";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "ProiectFacultateRecommender/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List> responseEntity = restTemplate.exchange(urlWiki, HttpMethod.GET, entity, List.class);
            List<Object> response = responseEntity.getBody();
            List<Course> wikiCourses = new ArrayList<>();

            if (response != null && response.size() >= 4) {
                List<String> titles = (List<String>) response.get(1);
                List<String> descriptions = (List<String>) response.get(2);
                List<String> links = (List<String>) response.get(3);

                for (int i = 0; i < titles.size(); i++) {
                    Course c = new Course();
                    c.setTitle(titles.get(i));
                    String d = (descriptions.size() > i && !descriptions.get(i).isEmpty())
                            ? descriptions.get(i) : "Curs teoretic despre " + titles.get(i);
                    c.setDescription(d);
                    c.setUrl(links.get(i));
                    wikiCourses.add(c);
                }
            }
            return wikiCourses;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/external-tracks")
    public ResponseEntity<List<String>> getExternalTracks() {
        List<Course> courses = externalCourseService.fetchExternalCourses();
        return ResponseEntity.ok(courses.stream().map(Course::getTitle).collect(Collectors.toList()));
    }
}