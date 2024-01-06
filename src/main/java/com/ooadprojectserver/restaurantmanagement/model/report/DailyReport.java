package com.ooadprojectserver.restaurantmanagement.model.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_FORMAT;

@Getter
@Setter
@Entity
@Table(name = "daily_revenue")
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = DATE_FORMAT, timezone = DateTimeConstant.TIMEZONE)
    private Date date;

    @Column(name = "revenue", precision = 38, scale = 2)
    private BigDecimal revenue;

    @Column(name = "num_of_cus")
    private Long numOfCustomers;

    @Column(name = "num_of_orders")
    private Long numOfOrders;

}