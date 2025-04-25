package com.zarubovandlevchenko.lb1.config;

import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mapping.model.Property;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import javax.transaction.UserTransaction;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({UsersDbProperties.class, DataDbProperties.class})
public class JpaConfig {

    private final DataDbProperties dataDbProperties;
    private final UsersDbProperties usersDbProperties;

    public JpaConfig(DataDbProperties dataDbProperties, UsersDbProperties usersDbProperties) {
        this.dataDbProperties = dataDbProperties;
        this.usersDbProperties = usersDbProperties;
    }

    @EnableJpaRepositories(
            basePackages = "com.zarubovandlevchenko.lb1.repository.dbcard",
            entityManagerFactoryRef = "dataDbEntityManagerFactory",
            transactionManagerRef = "transactionManager"
    )
    public static class DataDbJpaConfig {
    }

    // Конфигурация для users_db репозиториев
    @EnableJpaRepositories(
            basePackages = "com.zarubovandlevchenko.lb1.repository.dbuser",
            entityManagerFactoryRef = "usersDbEntityManagerFactory",
            transactionManagerRef = "transactionManager"
    )
    public static class UsersDbJpaConfig {
    }

    @Bean(name = "dataDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dataDbEntityManagerFactory(
            @Qualifier("dataDbDataSource") DataSource dataSource
            ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.zarubovandlevchenko.lb1.model.dbcard");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(buildJpaProperties(dataDbProperties));
        return em;
    }

    @Bean(name = "usersDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usersDbEntityManagerFactory(
            @Qualifier("usersDbDataSource") DataSource dataSource
            ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.zarubovandlevchenko.lb1.model.dbuser");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(buildJpaProperties(usersDbProperties));
        return em;
    }

    private Map<String, Object> buildJpaProperties(DbProperties dbProperties) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dbProperties.getDialect());
        properties.put("hibernate.hbm2ddl.auto", dbProperties.getHbm2ddlAuto());

        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.put("javax.persistence.transactionType", "JTA");
        return properties;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(300); // Таймаут транзакции в секундах
        userTransactionManager.setForceShutdown(true); // Принудительное завершение при остановке
        return userTransactionManager;
    }

    @Bean()
    public JtaTransactionManager transactionManager(UserTransactionManager userTransactionManager) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager);
        jtaTransactionManager.setUserTransaction( userTransactionManager);
        return jtaTransactionManager;
    }
}
