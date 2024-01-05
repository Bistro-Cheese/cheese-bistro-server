package com.ooadprojectserver.restaurantmanagement.model.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Order order;

    //TODO: move discount to order
//    @ManyToOne
//    @JoinColumn(name = "discount_id")
//    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "pay_id")
    private Payment payment;

    //TODO: move customer to order
//    @ManyToOne
//    @JoinColumn(name = "cus_id")
//    private Customer customer;

    @Column(name = "total")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private BigDecimal total;

    //TODO: remove subTotal
//    @Column(name = "sub_total")
//    @JdbcTypeCode(SqlTypes.DECIMAL)
//    private BigDecimal subTotal;

    @Column(name = "paid")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private BigDecimal paid;

    @Column(name = "change_paid")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private BigDecimal change;

    //TODO: remove subTotal
//    @Column(name = "deposit")
//    @JdbcTypeCode(SqlTypes.DECIMAL)
//    private BigDecimal deposit;

    //TODO: Remove status
//    @Column(name = "status")
//    @Enumerated(value = EnumType.ORDINAL)
//    private BillStatus status;

    @CreatedDate
    @Column(name = "cus_in")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    private Date cusIn;

    @LastModifiedDate
    @Column(name = "cus_out")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    private Date cusOut;

}