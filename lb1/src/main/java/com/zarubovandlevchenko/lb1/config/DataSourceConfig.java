//package com.zarubovandlevchenko.lb1.config;
//
//import com.atomikos.jdbc.AtomikosDataSourceBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableConfigurationProperties({UsersDbProperties.class, DataDbProperties.class})
//public class DataSourceConfig {
//
//    private final DataDbProperties dataDbProperties;
//    private final UsersDbProperties usersDbProperties;
//
//    public DataSourceConfig(DataDbProperties dataDbProperties, UsersDbProperties usersDbProperties) {
//        this.dataDbProperties = dataDbProperties;
//        this.usersDbProperties = usersDbProperties;
//    }
//
//    @Bean(name = "dataDbDataSource")
//    public DataSource dataDbDataSource() {
//        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
//        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
//        ds.setUniqueResourceName("data_db");
//        ds.setXaProperties(buildXaProperties(dataDbProperties));
//        ds.setPoolSize(5);
//        return ds;
//    }
//
//    @Bean(name = "usersDbDataSource")
//    public DataSource usersDbDataSource() {
//        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
//        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
//        ds.setUniqueResourceName("users_db");
//        ds.setXaProperties(buildXaProperties(usersDbProperties));
//        ds.setPoolSize(5);
//        return ds;
//    }
//
//    private Properties buildXaProperties(DataDbProperties properties) {
//        Properties xaProperties = new Properties();
//        xaProperties.setProperty("url", properties.getUrl());
//        xaProperties.setProperty("user", properties.getUsername());
//        xaProperties.setProperty("password", properties.getPassword());
//        return xaProperties;
//    }
//
//    private Properties buildXaProperties(UsersDbProperties properties) {
//        Properties xaProperties = new Properties();
//        xaProperties.setProperty("url", properties.getUrl());
//        xaProperties.setProperty("user", properties.getUsername());
//        xaProperties.setProperty("password", properties.getPassword());
//        return xaProperties;
//    }
//}