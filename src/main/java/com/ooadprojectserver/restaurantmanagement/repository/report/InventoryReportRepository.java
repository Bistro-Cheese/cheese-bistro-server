package com.ooadprojectserver.restaurantmanagement.repository.report;

import com.ooadprojectserver.restaurantmanagement.model.report.InventoryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;



@Repository
public interface InventoryReportRepository extends JpaRepository<InventoryReport, Integer> {
    List<InventoryReport> findByOperationDate(Date date);
}
