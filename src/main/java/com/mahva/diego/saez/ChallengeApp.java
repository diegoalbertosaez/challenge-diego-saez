package com.mahva.diego.saez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main class for application.
 * 
 * @author diegosaez
 *
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableTransactionManagement
public class ChallengeApp {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApp.class, args);
	}

}
