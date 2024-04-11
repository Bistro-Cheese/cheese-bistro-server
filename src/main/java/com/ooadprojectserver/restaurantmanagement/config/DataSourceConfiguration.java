package com.ooadprojectserver.restaurantmanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 4/9/2024, Tuesday
 * @description:
 **/

@Configuration
public class DataSourceConfiguration {

    private final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);


    /*
    * Initial DataSource for MySQL
    * */
    @Bean
    DataSource bistroMySqlDataSource() {
        return bistroMySQLDataSource().initializeDataSourceBuilder().build();
    }

    /*
    * Please refer to application.yml to see the properties
    * bistro:
    *   database:
    *       mysql:
    * */
    @Bean
    @ConfigurationProperties(prefix = "bistro.database.mysql")
    DataSourceProperties bistroMySQLDataSource() {
        return new DataSourceProperties();
    }

    /*
    * Log all the DataSource
    * */
    @Bean
    ApplicationRunner javaConfigDbRunner(Map<String, DataSource> dataSource) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                dataSource.forEach((key, db) -> {
                    logger.info(key + '=' + db);
                });
            }
        };
    }

}
