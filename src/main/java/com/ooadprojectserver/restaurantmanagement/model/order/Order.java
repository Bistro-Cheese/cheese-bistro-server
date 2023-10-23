package com.ooadprojectserver.restaurantmanagement.model.order;

import com.ooadprojectserver.restaurantmanagement.model.order.payment.PaymentMethod;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Staff;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@jakarta.persistence.Table(name = "restaurant_order")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff user;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private OrderTable orderTable;

    @Column(name = "order_date", nullable = false)
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "total", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long total;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private OrderStatus status;

}