package com.ooadprojectserver.restaurantmanagement.model.report;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "yearly_revenue")
public class YearlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "revenue", precision = 38, scale = 2)
    private BigDecimal revenue;

    @Column(name = "num_of_cus")
    private Long numOfCustomers;

    @Column(name = "num_of_orders")
    private Long numOfOrders;
}
