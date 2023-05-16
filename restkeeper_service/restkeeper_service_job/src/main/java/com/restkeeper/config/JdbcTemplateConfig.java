package com.restkeeper.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig{
    @Value("${spring.datasource.store.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.store.password}")
    private String password;

    @Value("${spring.datasource.store.url}")
    private String url;

    @Value("${spring.datasource.store.username}")
    private String username;

    @Bean(name = "storeDataSourceProperties")
    @ConfigurationProperties(prefix="spring.datasource.store")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean(name = "storeDataSource")
    public DataSource storeDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .password(password)
                .username(username)
                .build();
    }

    @Bean(name="storeJdbcTemplate")
    public JdbcTemplate storeJdbcTemplate(@Qualifier("storeDataSource" ) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "storeTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("storeDataSource" ) DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}

