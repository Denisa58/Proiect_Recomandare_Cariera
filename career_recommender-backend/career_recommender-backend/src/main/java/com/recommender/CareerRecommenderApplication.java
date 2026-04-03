package com.recommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clasa principala care porneste intreaga aplicatie
 */
@SpringBootApplication //adnotarea ce activeaza spring boot
public class CareerRecommenderApplication {

	public static void main(String[] args) {
        //incarca toate setarile si porneste serverul
		SpringApplication.run(CareerRecommenderApplication.class, args);
	}

}
