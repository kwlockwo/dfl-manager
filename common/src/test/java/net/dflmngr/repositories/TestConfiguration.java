package net.dflmngr.repositories;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
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
     * Passes Hibernate properties from application-test.yml to ensure dialect auto-detection.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource,
            @Autowired Environment env) {

        // Build Hibernate properties from application-test.yml
        Map<String, Object> properties = new HashMap<>();

        // Pass JDBC URL to Hibernate so it can detect the dialect
        properties.put("jakarta.persistence.jdbc.url", env.getProperty("spring.datasource.url"));
        properties.put("jakarta.persistence.jdbc.user", env.getProperty("spring.datasource.username"));
        properties.put("jakarta.persistence.jdbc.password", env.getProperty("spring.datasource.password"));
        properties.put("jakarta.persistence.jdbc.driver", env.getProperty("spring.datasource.driver-class-name"));

        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "create-drop"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql", "true"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql", "true"));
        properties.put("hibernate.use_sql_comments", env.getProperty("spring.jpa.properties.hibernate.use_sql_comments", "true"));

        LocalContainerEntityManagerFactoryBean emfb = builder
                .dataSource(dataSource)
                .packages("net.dflmngr.model.entity")
                .persistenceUnit("test")
                .properties(properties)
                .build();

        // Explicitly set to use jakarta.persistence.EntityManagerFactory to avoid proxy conflict
        emfb.setEntityManagerFactoryInterface(EntityManagerFactory.class);

        return emfb;
    }
}
