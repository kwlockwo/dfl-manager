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
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

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
     * Uses HibernateJpaVendorAdapter to enable database platform auto-detection.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            @Autowired Environment env) {

        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setPackagesToScan("net.dflmngr.model.entity");
        emfb.setPersistenceUnitName("test");

        // Use HibernateJpaVendorAdapter with database platform detection
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("spring.jpa.show-sql", "true")));

        // Detect database platform from driver class
        String driverClass = env.getProperty("spring.datasource.driver-class-name", "org.h2.Driver");
        if (driverClass.contains("postgresql")) {
            vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        } else if (driverClass.contains("h2")) {
            vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        }

        emfb.setJpaVendorAdapter(vendorAdapter);

        // Build Hibernate properties from application-test.yml
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "create-drop"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql", "true"));
        properties.put("hibernate.use_sql_comments", env.getProperty("spring.jpa.properties.hibernate.use_sql_comments", "true"));
        emfb.setJpaPropertyMap(properties);

        // Explicitly set to use jakarta.persistence.EntityManagerFactory to avoid proxy conflict
        emfb.setEntityManagerFactoryInterface(EntityManagerFactory.class);

        return emfb;
    }
}
