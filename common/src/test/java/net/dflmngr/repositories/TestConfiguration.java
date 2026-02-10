package net.dflmngr.repositories;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Test configuration for repository integration tests.
 *
 * Enables Spring Boot, JPA, and component scanning for test context.
 * Explicitly configures EntityManagerFactory to use jakarta.persistence.EntityManagerFactory interface.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "net.dflmngr.repositories")
@EntityScan(basePackages = "net.dflmngr.model.entity")
public class TestConfiguration {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        LocalContainerEntityManagerFactoryBean emfb = builder
                .dataSource(dataSource)
                .packages("net.dflmngr.model.entity")
                .persistenceUnit("test")
                .properties(properties)
                .build();

        // Explicitly set to use jakarta.persistence.EntityManagerFactory
        emfb.setEntityManagerFactoryInterface(EntityManagerFactory.class);

        return emfb;
    }
}
