package com.cultur.eventmanager.configurations;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.cultur.eventmanager.repositories"})
@ComponentScan(basePackages = { "com.cultur.eventmanager", "com.cultur.eventmanager.repositories", "com.cultur.eventmanager.services", "com.cultur.eventmanager.controllers" })
@PropertySource(value = {"classpath:application.properties"})
public class HibernateConfiguration {

    private Logger logger = Logger.getLogger(HibernateConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        logger.info("Datasource initializing.....");
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

        try {
            comboPooledDataSource.setDriverClass(environment.getProperty("datasource.driver.class"));
            comboPooledDataSource.setJdbcUrl(environment.getProperty("datasource.url"));
            comboPooledDataSource.setUser(environment.getProperty("datasource.username"));
            comboPooledDataSource.setPassword(environment.getProperty("datasource.password"));
            comboPooledDataSource.setMinPoolSize(Integer.parseInt(environment.getProperty("datasource.min.pool.size")));
            comboPooledDataSource.setInitialPoolSize(Integer.parseInt(environment.getProperty("datasource.initial.pool.size")));
            comboPooledDataSource.setMaxPoolSize(Integer.parseInt(environment.getProperty("datasource.max.pool.size")));
            comboPooledDataSource.setAcquireIncrement(Integer.parseInt(environment.getProperty("datasource.acquire.increment")));
            comboPooledDataSource.setIdleConnectionTestPeriod(Integer.parseInt(environment.getProperty("datasource.idle.connection.test.period")));
            comboPooledDataSource.setMaxStatements(Integer.parseInt(environment.getProperty("datasource.max.statements")));
            comboPooledDataSource.setMaxStatementsPerConnection(Integer.parseInt(environment.getProperty("datasource.max.statements.per.connection")));
            comboPooledDataSource.setMaxIdleTime(Integer.parseInt(environment.getProperty("datasource.max.idle.time")));
            comboPooledDataSource.setMaxIdleTimeExcessConnections(Integer.parseInt(environment.getProperty("datasource.max.idle.time.excess.connections")));
            logger.info("Datasource created.....");
        } catch (PropertyVetoException e) {
            logger.error("Exception occurs while initalizing drive class", e);
        }

        return comboPooledDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        logger.info("Initializing JpaTransactionManager...");
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        logger.info("JpaTransactionManager initialized...");

        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        logger.info("Initializing LocalContainerEntityManagerFacpotoryBean...");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.cultur.eventmanager.entities" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        logger.info("LocalContainerEntityManagerFactoryBean initialized...");

        return em;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl"));
        properties.setProperty("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));

        return properties;
    }

    @Bean
    public HibernatePersistenceProvider persistenceProvider() {
        return new HibernatePersistenceProvider();
    }
}

