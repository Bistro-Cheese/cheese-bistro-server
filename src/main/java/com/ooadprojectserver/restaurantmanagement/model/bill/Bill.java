package com.ooadprojectserver.restaurantmanagement.model.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.Payment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "pay_id")
    private Payment payment;

    @CreatedDate
    @Column(name = "cus_in")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    private Date customerIn;

    @LastModifiedDate
    @Column(name = "cus_out")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    private Date customerOut;

    @Column(name = "fax")
    private Double fax;

    @Column(name = "total")
    private BigDecimal total;
}