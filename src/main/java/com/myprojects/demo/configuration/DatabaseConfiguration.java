package com.myprojects.demo.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DatabaseConfiguration {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/vesselpro");
        dataSource.setUsername("vesselpro");
        dataSource.setPassword("vesselpro");
        dataSource.setAutoCommit(false);
        dataSource.setMinimumIdle(50);
        dataSource.setMaximumPoolSize(500);
        dataSource.setPoolName("Postgres-Pool");
        return dataSource;
    }

}
