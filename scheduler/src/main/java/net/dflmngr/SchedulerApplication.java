package net.dflmngr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application for the DFL Manager Scheduler module.
 *
 * Note: In Phase 1, this application provides Spring Boot infrastructure
 * but scheduler code still uses EclipseLink JPA and manual service instantiation.
 * In Phase 2, we will migrate to Spring Data JPA and Spring-managed services.
 */
@SpringBootApplication
public class SchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}
}
