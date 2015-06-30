package com.yo1000.vis;

import com.yo1000.vis.component.aop.Cache;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@SpringBootApplication
@EnableScheduling
public class ApplicationContext {
    public static final String PROPS_DATA_SYSTEM_JDBC = "vis.data.system.jdbc";
    public static final String PROPS_DATA_CHART_JDBC = "vis.data.chart.jdbc";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationContext.class, args);
    }

    @Bean
    @Autowired
    public RestTemplate restTemplate(
            @Qualifier("httpClient") HttpClient httpClient
    ) throws Exception {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);

        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();
    }

    @Bean
    public Cache.Store<String> cacheStore() {
        return new Cache.Store<String>();
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
