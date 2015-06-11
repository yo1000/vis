package com.yo1000.vis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@SpringBootApplication
public class ApplicationContext {
    public static final String PROPS_DATA_SYSTEM_JDBC = "vis.data.system.jdbc";
    public static final String PROPS_DATA_CHART_JDBC = "vis.data.chart.jdbc";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationContext.class, args);
    }

    @Bean
    @Autowired
    public JdbcTemplate systemJdbcTemplate(
            @Qualifier("systemJdbcConfiguration") JdbcConfiguration jdbcConfiguration
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcConfiguration.getDriverClassName());
        dataSource.setUrl(jdbcConfiguration.getUrl());
        dataSource.setUsername(jdbcConfiguration.getUsername());
        dataSource.setPassword(jdbcConfiguration.getPassword());

        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate chartJdbcTemplate(
            @Qualifier("chartJdbcConfiguration") JdbcConfiguration jdbcConfiguration
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcConfiguration.getDriverClassName());
        dataSource.setUrl(jdbcConfiguration.getUrl());
        dataSource.setUsername(jdbcConfiguration.getUsername());
        dataSource.setPassword(jdbcConfiguration.getPassword());

        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = PROPS_DATA_SYSTEM_JDBC)
    public JdbcConfiguration systemJdbcConfiguration() {
        return new JdbcConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = PROPS_DATA_CHART_JDBC)
    public JdbcConfiguration chartJdbcConfiguration() {
        return new JdbcConfiguration();
    }

    @SpringBootApplication
    public static class JdbcConfiguration {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private String schema;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }
    }
}
