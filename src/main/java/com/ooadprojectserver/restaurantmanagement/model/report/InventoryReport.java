package com.ooadprojectserver.restaurantmanagement.model.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_FORMAT2;

@Getter
@Setter
@Entity
@Table(name = "inventory_report")
public class InventoryReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(name = "supplier", nullable = false)
    private String supplier;

    @Column(name = "unit", nullable = false, length = 45)
    private String unit;

    @Column(name = "quantity", nullable = false)
    private Float quantity;

    @Column(name = "import_quantity", nullable = false)
    private Double importQuantity;

    @Column(name = "export_quantity", nullable = false)
    private Double exportQuantity;

    @Column(name = "operation_date")
    @DateTimeFormat(pattern = DATE_FORMAT2)
    private LocalDate operationDate;

}