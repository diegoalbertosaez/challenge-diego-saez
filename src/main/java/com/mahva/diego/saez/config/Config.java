package com.mahva.diego.saez.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application.
 * 
 * @author diegosaez
 *
 */
@Configuration
public class Config {

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

}
