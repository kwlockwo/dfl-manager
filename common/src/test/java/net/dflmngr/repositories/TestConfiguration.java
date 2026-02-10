package net.dflmngr.repositories;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/**
 * Test configuration for repository integration tests.
 *
 * Enables Spring Boot, JPA, and component scanning for test context.
 * Explicitly configures EntityManagerFactory to avoid Hibernate SessionFactory proxy issues.
 * Database configuration comes from application-test.yml (H2 locally, PostgreSQL in CI).
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "net.dflmngr.repositories")
@EntityScan(basePackages = "net.dflmngr.model.entity")
public class TestConfiguration {

    /**
     * Custom EntityManagerFactory bean to avoid Hibernate 6.6.8 SessionFactory proxy conflict.
     * Explicitly sets entityManagerFactoryInterface to jakarta.persistence.EntityManagerFactory.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean emfb = builder
                .dataSource(dataSource)
                .packages("net.dflmngr.model.entity")
                .persistenceUnit("test")
                .build();

        // Explicitly set to use jakarta.persistence.EntityManagerFactory to avoid proxy conflict
        emfb.setEntityManagerFactoryInterface(EntityManagerFactory.class);

        return emfb;
    }
}
