package com.ooadprojectserver.restaurantmanagement.config.datasource;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 4/9/2024, Tuesday
 * @description:
 **/

@Configuration
@RequiredArgsConstructor
public class DataSourceRoutingConfiguration {

    private final Logger logger = LoggerFactory.getLogger(DataSourceRoutingConfiguration.class);

    public final static String MASTER = "master datasource";
    public final static String SLAVE = "slave datasource";



    public static ThreadLocal<String> ACTIVE_DATASOURCE
            = new ThreadLocal<>();

    @Bean
    @Primary
    RedundancyDataSourceManager theOneDataSource(
            DataSource bistroMySqlMasterDataSource
    ) {
        return new RedundancyDataSourceManager(
                Map.of(MASTER, bistroMySqlMasterDataSource)
        );
    }

    static class RedundancyDataSourceManager
        extends AbstractRoutingDataSource{

        RedundancyDataSourceManager (Map<String, DataSource> dataSource) {
            Map<Object, Object> newMap = new HashMap<>(dataSource);
            newMap.putAll(dataSource);
            this.setTargetDataSources(newMap);
        }

        @Override
        protected Object determineCurrentLookupKey() {
            if (ACTIVE_DATASOURCE.get() == null)
                ACTIVE_DATASOURCE.set(MASTER);

            return ACTIVE_DATASOURCE.get();
        }
    }

    @Bean
    DataSource bistroMySqlMasterDataSource() {
        return bistroMasterMySQLDataSource().initializeDataSourceBuilder().build();
    }

//    @Bean
//    DataSource bistroMySqlSlaveDataSource() {
//        return bistroSlaveMySQLDataSource().initializeDataSourceBuilder().build();
//    }

    /*
     * Please refer to application.yml to see the properties
     * bistro:
     *   database:
     *       mysql:
     * */
    @Bean
    @ConfigurationProperties(prefix = "bistro.master.database.mysql")
    DataSourceProperties bistroMasterMySQLDataSource() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "bistro.slave.database.mysql")
    DataSourceProperties bistroSlaveMySQLDataSource() {
        return new DataSourceProperties();
    }

    private  ResourceDatabasePopulator getResourceDatabasePopulator(){
        return new ResourceDatabasePopulator();
    }

    /*
     * Log all the DataSource
     * */
    @Bean
    ApplicationRunner javaConfigDataSourceRunner(Map<String, DataSource> dataSource) {
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
