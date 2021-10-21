package com.lguplus.ukids.admin.config;

import javax.sql.DataSource;

import com.lguplus.ukids.admin.exception.SystemException;

import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;

@Configuration
public class DataSourceConfig {

    @Value("${resources.root}${resources.path.admin-endpoint}")
    private Resource endpoint;

    @Value("${resources.root}${resources.path.admin-username}")
    private Resource username;

    @Value("${resources.root}${resources.path.admin-password}")
    private Resource password;

    @Value("${resources.root}${resources.path.admin-database}")
    private Resource database;

    @Value("${resources.root}${resources.path.admin-schema}")
    private Resource schema;

    @Value("${resources.driver-class}")
    private String driverClass;

    @Bean
    @Primary
    @FlywayDataSource
    public DataSource dataSource() throws SystemException {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        try {
            dataSourceBuilder.driverClassName(driverClass);
            String dataSourceUrl = new String(this.endpoint.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                    .trim();
            String databaseName = new String(this.database.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            String schemaName = new String(this.schema.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            dataSourceUrl = "jdbc:postgresql://" + dataSourceUrl
                    + "/" + databaseName + "?currentSchema=" + schemaName + "&charSet=utf8";

            dataSourceBuilder.url(dataSourceUrl);
            String username = new String(this.username.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            dataSourceBuilder.username(username);
            String password = new String(this.password.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            dataSourceBuilder.password(password);
        } catch (Exception exception) {
            throw new SystemException(exception.getMessage());
        }

        return dataSourceBuilder.build();
    }
}