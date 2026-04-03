package com.recommender.service;

import com.recommender.model.*;
import com.recommender.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.recommender.repository.CourseRepository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Clasa cu rolul de a popula baza de date la prima rulare a aplicatiei cu skill-uri cariere si cerintele carierelor
 * Implementeaza interfata CommandLineRunner pentru a asigura executia logicii de configurare a setului de date initial
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final MasterSkillRepository masterSkillRepository;
    private final CareerRepository careerRepository;
    private final CareerSkillRequirementRepository requirementRepository;
    private final CourseRepository courseRepository;

    public DataInitializer(
            MasterSkillRepository masterSkillRepository,
            CareerRepository careerRepository,
            CareerSkillRequirementRepository requirementRepository,
            CourseRepository courseRepository) {

        this.masterSkillRepository = masterSkillRepository;
        this.careerRepository = careerRepository;
        this.requirementRepository = requirementRepository;
        this.courseRepository = courseRepository;


    }

    //la rulare verificam si inseram pe rand skill-urile,carierele si cursurile
    @Override
    public void run(String... args) throws Exception {

        initializeMasterSkills();
        initializeCareersAndRequirements();

    }

    private void initializeMasterSkills() {


        if (masterSkillRepository.count() == 0) {

            List<MasterSkill> initialSkills = Arrays.asList(

                    new MasterSkill("Java", "Technical"),
                    new MasterSkill("Python", "Technical"),
                    new MasterSkill("SQL", "Technical"),
                    new MasterSkill("JavaScript", "Technical"),
                    new MasterSkill("Cloud Computing (AWS/Azure)", "Technical"),
                    new MasterSkill("Data Analysis (R/Pandas)", "Technical"),
                    new MasterSkill("C++", "Technical"),
                    new MasterSkill("Linux/Shell Scripting", "Technical"),
                    new MasterSkill("Cybersecurity Fundamentals", "Technical"),
                    new MasterSkill("Game Design", "Technical"),


                    new MasterSkill("Leadership", "Soft Skill"),
                    new MasterSkill("Communication", "Soft Skill"),
                    new MasterSkill("Problem Solving", "Soft Skill"),
                    new MasterSkill("Teamwork", "Soft Skill"),
                    new MasterSkill("Time Management", "Soft Skill"),
                    new MasterSkill("UI/UX Design", "Soft Skill"),


                    new MasterSkill("Project Management (Agile)", "Management"),
                    new MasterSkill("Budgeting", "Management"),
                    new MasterSkill("Marketing Strategy", "Management"),
                    new MasterSkill("Financial Modeling", "Management")
            );

            masterSkillRepository.saveAll(initialSkills);
            System.out.println(">>> " + initialSkills.size() + " Master Skills inițiale au fost inserate.");
        }
    }

    private void initializeCareersAndRequirements() {
        if (careerRepository.count() == 0) {

            Career javaDev = new Career("Dezvoltator Java Backend", "Responsabil cu arhitectura și logica de business (Spring Boot, Microservicii).", "Mid-Level", 80000);
            Career dataAnalyst = new Career("Analist Date și BI", "Procesarea datelor mari, vizualizare și raportare (SQL, Python).", "Entry Level", 60000);
            Career projectManager = new Career("Manager de Proiect Agile", "Planificarea, execuția și închiderea proiectelor software (Scrum/Kanban).", "Senior", 95000);


            Career frontendDev = new Career("Dezvoltator Frontend React", "Crearea și întreținerea interfețelor grafice (UI/UX).", "Mid-Level", 75000);
            Career dataScientist = new Career("Data Scientist/ML Engineer", "Modele predictive și învățare automată.", "Senior", 100000);
            Career marketingSpecialist = new Career("Specialist Marketing Digital", "Elaborarea și implementarea strategiilor de marketing online.", "Entry Level", 50000);
            Career devOps = new Career("DevOps Engineer", "Automatizarea pipeline-urilor de CI/CD și administrarea infrastructurii (IaC, Kubernetes).", "Mid-Senior", 90000);
            Career securityEngineer = new Career("Security Engineer", "Implementarea, monitorizarea și testarea sistemelor de securitate.", "Senior", 110000);
            Career gameDeveloper = new Career("Game Developer C++", "Dezvoltarea logicii de joc și a componentelor grafice (Unreal/Unity).", "Mid-Level", 85000);
            Career uxDesigner = new Career("UI/UX Designer", "Proiectarea experienței utilizatorului și a interfețelor intuitive.", "Mid-Level", 70000);
            Career financialAnalyst = new Career("Analist Financiar", "Evaluarea performanței financiare și a oportunităților de investiții.", "Entry Level", 65000);
            Career cloudArchitect = new Career("Cloud Solutions Architect", "Proiectarea soluțiilor scalabile și securizate în mediul cloud.", "Senior", 120000);

            careerRepository.saveAll(List.of(javaDev, dataAnalyst, projectManager, frontendDev, dataScientist, marketingSpecialist, devOps, securityEngineer, gameDeveloper,
                    uxDesigner, financialAnalyst, cloudArchitect));

            Optional<MasterSkill> javaOpt = masterSkillRepository.findBySkillName("Java");
            Optional<MasterSkill> sqlOpt = masterSkillRepository.findBySkillName("SQL");
            Optional<MasterSkill> pythonOpt = masterSkillRepository.findBySkillName("Python");
            Optional<MasterSkill> jsOpt = masterSkillRepository.findBySkillName("JavaScript");
            Optional<MasterSkill> cloudOpt = masterSkillRepository.findBySkillName("Cloud Computing (AWS/Azure)");
            Optional<MasterSkill> dataAnalysisOpt = masterSkillRepository.findBySkillName("Data Analysis (R/Pandas)");
            Optional<MasterSkill> pmOpt = masterSkillRepository.findBySkillName("Project Management (Agile)");
            Optional<MasterSkill> leadershipOpt = masterSkillRepository.findBySkillName("Leadership");
            Optional<MasterSkill> problemSolvingOpt = masterSkillRepository.findBySkillName("Problem Solving");
            Optional<MasterSkill> marketingStrategyOpt = masterSkillRepository.findBySkillName("Marketing Strategy");
            Optional<MasterSkill> linuxOpt = masterSkillRepository.findBySkillName("Linux/Shell Scripting");
            Optional<MasterSkill> securityOpt = masterSkillRepository.findBySkillName("Cybersecurity Fundamentals");
            Optional<MasterSkill> gameDesignOpt = masterSkillRepository.findBySkillName("Game Design");
            Optional<MasterSkill> uiUxOpt = masterSkillRepository.findBySkillName("UI/UX Design");
            Optional<MasterSkill> financeOpt = masterSkillRepository.findBySkillName("Financial Modeling");
            Optional<MasterSkill> cPlusPlusOpt = masterSkillRepository.findBySkillName("C++");
            Optional<MasterSkill> teamworkOpt = masterSkillRepository.findBySkillName("Teamwork");


            javaOpt.ifPresent(java -> {
                requirementRepository.save(new CareerSkillRequirement(javaDev, java, 4));
                sqlOpt.ifPresent(sql -> requirementRepository.save(new CareerSkillRequirement(javaDev, sql, 3)));
            });
            leadershipOpt.ifPresent(leadership -> requirementRepository.save(new CareerSkillRequirement(javaDev, leadership, 2)));


            jsOpt.ifPresent(js -> {
                requirementRepository.save(new CareerSkillRequirement(frontendDev, js, 5));      // JavaScript: Nivel 5 (Crucial)
                cloudOpt.ifPresent(cloud -> requirementRepository.save(new CareerSkillRequirement(frontendDev, cloud, 2))); // Cloud: Nivel 2
                problemSolvingOpt.ifPresent(ps -> requirementRepository.save(new CareerSkillRequirement(frontendDev, ps, 4)));
            });

            pythonOpt.ifPresent(python -> {
                requirementRepository.save(new CareerSkillRequirement(dataScientist, python, 5));
                dataAnalysisOpt.ifPresent(da -> requirementRepository.save(new CareerSkillRequirement(dataScientist, da, 4)));
            });
            sqlOpt.ifPresent(sql -> requirementRepository.save(new CareerSkillRequirement(dataScientist, sql, 3)));


            pythonOpt.ifPresent(python -> requirementRepository.save(new CareerSkillRequirement(dataAnalyst, python, 3)));
            sqlOpt.ifPresent(sql -> requirementRepository.save(new CareerSkillRequirement(dataAnalyst, sql, 4)));
            problemSolvingOpt.ifPresent(ps -> requirementRepository.save(new CareerSkillRequirement(dataAnalyst, ps, 3)));


            pmOpt.ifPresent(pm -> requirementRepository.save(new CareerSkillRequirement(projectManager, pm, 5)));
            leadershipOpt.ifPresent(leadership -> requirementRepository.save(new CareerSkillRequirement(projectManager, leadership, 4)));


            marketingStrategyOpt.ifPresent(ms -> requirementRepository.save(new CareerSkillRequirement(marketingSpecialist, ms, 4)));
            jsOpt.ifPresent(js -> requirementRepository.save(new CareerSkillRequirement(marketingSpecialist, js, 2))); // Cunoștințe de bază JS
            problemSolvingOpt.ifPresent(ps -> requirementRepository.save(new CareerSkillRequirement(marketingSpecialist, ps, 3)));


            linuxOpt.ifPresent(linux -> {
                requirementRepository.save(new CareerSkillRequirement(devOps, linux, 5)); // Linux: Crucial
                cloudOpt.ifPresent(cloud -> requirementRepository.save(new CareerSkillRequirement(devOps, cloud, 4)));
                pythonOpt.ifPresent(python -> requirementRepository.save(new CareerSkillRequirement(devOps, python, 3)));
                teamworkOpt.ifPresent(teamwork -> requirementRepository.save(new CareerSkillRequirement(devOps, teamwork, 4)));
            });

            securityOpt.ifPresent(security -> {
                requirementRepository.save(new CareerSkillRequirement(securityEngineer, security, 5)); // Securitate: Crucial
                cloudOpt.ifPresent(cloud -> requirementRepository.save(new CareerSkillRequirement(securityEngineer, cloud, 3)));
                linuxOpt.ifPresent(linux -> requirementRepository.save(new CareerSkillRequirement(securityEngineer, linux, 3)));
                problemSolvingOpt.ifPresent(ps -> requirementRepository.save(new CareerSkillRequirement(securityEngineer, ps, 5)));
            });

            cPlusPlusOpt.ifPresent(cpp -> {
                requirementRepository.save(new CareerSkillRequirement(gameDeveloper, cpp, 5)); // C++: Crucial
                gameDesignOpt.ifPresent(gd -> requirementRepository.save(new CareerSkillRequirement(gameDeveloper, gd, 4)));
                problemSolvingOpt.ifPresent(ps -> requirementRepository.save(new CareerSkillRequirement(gameDeveloper, ps, 4)));
            });

            uiUxOpt.ifPresent(uiux -> {
                requirementRepository.save(new CareerSkillRequirement(uxDesigner, uiux, 5)); // UI/UX Design: Crucial
                uiUxOpt.ifPresent(comm -> requirementRepository.save(new CareerSkillRequirement(uxDesigner, comm, 4)));
                jsOpt.ifPresent(js -> requirementRepository.save(new CareerSkillRequirement(uxDesigner, js, 2))); // Baze de JavaScript
            });

            financeOpt.ifPresent(finance -> {
                requirementRepository.save(new CareerSkillRequirement(financialAnalyst, finance, 4));
                leadershipOpt.ifPresent(budget -> requirementRepository.save(new CareerSkillRequirement(financialAnalyst, budget, 3)));
                dataAnalysisOpt.ifPresent(da -> requirementRepository.save(new CareerSkillRequirement(financialAnalyst, da, 3)));
            });

            cloudOpt.ifPresent(cloud -> {
                requirementRepository.save(new CareerSkillRequirement(cloudArchitect, cloud, 5)); // Cloud: Crucial
                leadershipOpt.ifPresent(leadership -> requirementRepository.save(new CareerSkillRequirement(cloudArchitect, leadership, 4)));
                pmOpt.ifPresent(pm -> requirementRepository.save(new CareerSkillRequirement(cloudArchitect, pm, 3)));
            });


        }
    }

}