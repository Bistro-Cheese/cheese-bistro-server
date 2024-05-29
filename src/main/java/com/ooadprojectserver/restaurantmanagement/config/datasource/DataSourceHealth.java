package com.ooadprojectserver.restaurantmanagement.config.datasource;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 4/20/2024, Saturday
 * @description:
 **/

@Component
@RequiredArgsConstructor
public class DataSourceHealth implements HealthIndicator {

    private static final Logger logger = LogManager.getLogger(DataSourceHealth.class);

    private final Map<String, DataSource> dataSourceMap;
    private static final String MASTER_DATASOURCE = "bistroMySqlMasterDataSource";


    @Override
    public Health health() {
        try {
            if (checkMasterDatabaseConnection(dataSourceMap)) {
                return Health.up().build();
            } else {
                return Health.down().withDetail("reason", "Database connection failed").build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }

    private boolean checkMasterDatabaseConnection(Map<String, DataSource> dataSourceMap){
        try(Connection connection = dataSourceMap.get(MASTER_DATASOURCE).getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1");
            return true;
        }catch (SQLException e){
            logger.error("Master database crashed", e);
            return false;
        }
    }

    @Scheduled(fixedRate = 15000) // Check every 60 seconds
    public void checkDatabaseHealthAndSwitchIfNecessary() {
        Health health = this.health();
        if (health.getStatus() == Status.DOWN) {
            // Switch to slave database
            DataSourceRoutingConfiguration.ACTIVE_DATASOURCE.set(DataSourceRoutingConfiguration.SLAVE);
            logger.info("Switched to slave database due to master database being down.");
        }
    }
}
